package com.libraryapi.service.impl;

import com.libraryapi.config.LibraryProperties;
import com.libraryapi.dto.BorrowDTO;
import com.libraryapi.dto.BorrowRequestDTO;
import com.libraryapi.exception.BadRequestException;
import com.libraryapi.exception.NotFoundException;
import com.libraryapi.helper.BorrowHelper;
import com.libraryapi.mapper.BorrowMapper;
import com.libraryapi.model.*;
import com.libraryapi.repo.BorrowRepo;
import com.libraryapi.service.BorrowService;
import com.libraryapi.service.BorrowStatusService;
import com.libraryapi.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class BorrowServiceImpl implements BorrowService {

    private final BorrowRepo borrowRepo;
    private final BorrowMapper borrowMapper;
    private final BorrowHelper borrowHelper;
    private final LibraryProperties libraryProperties;
    // statuses are entities now: resolve BORROWED / OVERDUE / RETURNED by name
    private final BorrowStatusService borrowStatusService;

    private final MemberService memberService;

    @Override
    public BorrowDTO borrow(BorrowRequestDTO requestDTO) {

        // The stored OVERDUE flag can lag behind the due dates, so refresh it
        // before rule 4 reads the status column. MANDATORY-safe: this call
        // joins the transaction opened by this method.
        borrowHelper.refreshOverdue();

        Member member = memberService.getMemberEntityById(requestDTO.getMemberId());
        MemberType memberType = member.getType();

        // Rule 2: the active-borrow cap is data-driven (null = no limit).
        // "already holding the maximum" must be rejected, hence >=.
        Integer maxActiveBorrow = memberType.getMaxActiveBorrows();

        if(Objects.nonNull(maxActiveBorrow) && borrowRepo.countByMemberIdAndReturnDateIsNull(member.getId()) >= maxActiveBorrow){
            throw new BadRequestException("Member can have at most " + maxActiveBorrow + " active borrows");
        }

        // Rule 4: a member who still holds an overdue book cannot borrow.
        BorrowStatus overdueStatus = borrowStatusService.getBorrowStatusEntityByName("OVERDUE");

        if(borrowRepo.existsByMemberIdAndStatus(requestDTO.getMemberId(), overdueStatus)){
            throw new BadRequestException("Member with id " + requestDTO.getMemberId()
                    + " has an overdue book and cannot borrow");
        }

        // Rule 1: lock the book row (SELECT ... FOR UPDATE) so two concurrent
        // borrows cannot hand out the same last copy, then take one copy.
        Book book = borrowHelper.lockBookOrThrow(requestDTO.getBookId());

        if(book.getAvailableCopies() <= 0){
            throw new BadRequestException("No copies of book " + book.getId() + " are available right now");
        }

        // Rule 3: the loan period comes from the member type
        // (14 days for students, 30 for premium, ...).
        LocalDate borrowDate = LocalDate.now();
        int loanDays = memberType.getLoanDays();
        LocalDate dueDate = borrowDate.plusDays(loanDays);

        Borrow borrow = new Borrow();

        borrow.setBorrowDate(borrowDate);
        borrow.setDueDate(dueDate);
        borrow.setMember(member);
        borrow.setBook(book);

        // A fresh borrow always starts as BORROWED; the hourly refresh turns
        // it into OVERDUE once the due date passes.
        borrow.setStatus(borrowStatusService.getBorrowStatusEntityByName("BORROWED"));

        book.setAvailableCopies(book.getAvailableCopies() - 1);

        return borrowMapper.convertFromBorrowToBorrowDto(
                borrowRepo.save(borrow)
        );
    }

    @Override
    public BorrowDTO returnBook(Long id) {

        // Lock the borrow row so two concurrent returns cannot both pass the
        // "already returned" check and put the copy back twice.
        Borrow borrow = borrowHelper.lockBorrowOrThrow(id);

        // Rule 5: a borrow can be returned only once.
        if(Objects.nonNull(borrow.getReturnDate())){
            throw new BadRequestException("Borrow with id " + id + " is already returned");
        }

        BorrowStatus returnedStatus = borrowStatusService.getBorrowStatusEntityByName("RETURNED");

        // Rule 6: fine = days late * library.fine-per-day (rate from config).
        BigDecimal fine = BigDecimal.ZERO;
        LocalDate returnDate = LocalDate.now();
        int lateDays = 0;
        if(borrow.getDueDate().isBefore(returnDate)){
            lateDays = (int) Math.max(0, ChronoUnit.DAYS.between(borrow.getDueDate(), returnDate));
            fine = BigDecimal.valueOf(lateDays).multiply(libraryProperties.getFinePerDay());
        }

        borrow.setFine(fine.setScale(2, RoundingMode.HALF_UP));
        borrow.setReturnDate(returnDate);
        borrow.setOverdueDays(lateDays);
        borrow.setStatus(returnedStatus);

        // The copy goes back on the shelf.
        Book book = borrow.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);

        return borrowMapper.convertFromBorrowToBorrowDto(
                borrowRepo.save(borrow)
        );
    }

    // Reads refresh the stored flags first, so the list shows the current
    // OVERDUE state instead of waiting for the hourly job.
    @Override
    public List<BorrowDTO> getAllBorrows() {
        borrowHelper.refreshOverdue();
        List<Borrow> borrows = borrowRepo.findAllByOrderByIdDesc();

        if(borrows.isEmpty()){
            throw new NotFoundException("No borrows found");
        }

        return borrowMapper.convertFromBorrowListToBorrowDtoList(borrows);
    }

    @Override
    public List<BorrowDTO> getOverdueBorrows() {
        // Refresh first, then simply read the rows already flagged OVERDUE.
        borrowHelper.refreshOverdue();
        BorrowStatus overdueStatus = borrowStatusService.getBorrowStatusEntityByName("OVERDUE");
        List<Borrow> borrows = borrowRepo.findByStatus(overdueStatus);
        if (borrows.isEmpty()){
            throw new NotFoundException("No overdue borrows found");
        }
        return borrowMapper.convertFromBorrowListToBorrowDtoList(borrows);
    }

    @Transactional(readOnly = true)
    @Override
    public BorrowDTO getBorrowById(Long id) {
        // The helper already throws "Borrow with id X not found" when missing.
        return borrowMapper.convertFromBorrowToBorrowDto(borrowHelper.findBorrowOrThrow(id));
    }

    @Transactional(readOnly = true)
    @Override
    public List<BorrowDTO> getBorrowsByMember(Long memberId) {
        // History is newest first; an unknown/empty history is a 404.
        List<Borrow> borrows = borrowRepo.findByMemberIdOrderByIdDesc(memberId);

        if(borrows.isEmpty()){
            throw new NotFoundException("No borrows found for member with id " + memberId);
        }

        return borrowMapper.convertFromBorrowListToBorrowDtoList(borrows);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BorrowDTO> getBorrowsByBook(Long bookId) {
        // History is newest first; an unknown/empty history is a 404.
        List<Borrow> borrows = borrowRepo.findByBookIdOrderByIdDesc(bookId);

        if(borrows.isEmpty()){
            throw new NotFoundException("No borrows found for book with id " + bookId);
        }

        return borrowMapper.convertFromBorrowListToBorrowDtoList(borrows);
    }


}
