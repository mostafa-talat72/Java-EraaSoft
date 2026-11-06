package com.libraryapi.helper;

import com.libraryapi.exception.NotFoundException;
import com.libraryapi.model.Book;
import com.libraryapi.model.Borrow;
import com.libraryapi.model.BorrowStatus;
import com.libraryapi.repo.BookRepo;
import com.libraryapi.repo.BorrowRepo;
import com.libraryapi.service.BorrowStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Shared lookups and the overdue refresh used by BorrowServiceImpl.
 *
 * The lock methods rely on the caller's transaction: the row lock must stay
 * held until the service commits, so they are only called from inside a
 * transactional service method.
 */
@Component
@RequiredArgsConstructor
public class BorrowHelper {

    private final BorrowRepo borrowRepo;
    private final BookRepo bookRepo;
    private final BorrowStatusService borrowStatusService;

    // Plain read; no lock, used by getBorrowById.
    public Borrow findBorrowOrThrow(Long id) {
        return borrowRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Borrow with id " + id + " not found"));
    }

    // Rule 1: SELECT ... FOR UPDATE on the book row, so a second borrow of the
    // same book waits until this transaction commits and cannot oversell.
    @Transactional(propagation = Propagation.MANDATORY)
    public Book lockBookOrThrow(Long id){
        return bookRepo.findByIdForUpdate(id).orElseThrow(
                () -> new NotFoundException("Book with id " + id + " not found")
        );
    }

    // Locks the borrow row; joins the caller's transaction (REQUIRED), which is
    // what keeps the lock until the return is committed.
    public Borrow lockBorrowOrThrow(Long id){
        return borrowRepo.findByIdForUpdate(id).orElseThrow(
                () -> new NotFoundException("Borrow with id " + id + " not found")
        );
    }

    // Used by the delete flows: a book/member with borrow history cannot go.
    public boolean hasBorrowHistory(Long memberId){
        return borrowRepo.existsByMemberId(memberId);
    }

    public boolean hasBorrowHistoryForBook(Long bookId){
        return borrowRepo.existsByBookId(bookId);
    }

    // Keeps the stored status / overdueDays truthful. It runs from the hourly
    // job below and is also called by the services before reads and before
    // rule 4, so the stored flag never decides on stale data.
    // The entities are managed, so dirty checking already persists them; the
    // explicit save() just documents the intent.
    @Transactional
    @Scheduled(cron = "0 0 * * * *", zone = "Africa/Cairo")
    public void refreshOverdue(){

        List<Borrow> borrows = borrowRepo.findByReturnDateIsNullAndDueDateBefore(LocalDate.now());

        if(borrows.isEmpty())
            return;

        BorrowStatus borrowStatus = borrowStatusService.getBorrowStatusEntityByName("OVERDUE");

        for(Borrow borrow : borrows){
            borrow.setStatus(borrowStatus);
            borrow.setOverdueDays((int) ChronoUnit.DAYS.between(borrow.getDueDate(), LocalDate.now()));
            borrowRepo.save(borrow);
        }

    }
}
