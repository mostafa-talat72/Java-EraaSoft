package com.libraryapi.service.impl;

import com.libraryapi.config.LibraryProperties;
import com.libraryapi.dto.BorrowDTO;
import com.libraryapi.dto.BorrowRequestDTO;
import com.libraryapi.exception.BadRequestException;
import com.libraryapi.exception.NotFoundException;
import com.libraryapi.helper.BorrowHelper;
import com.libraryapi.mapper.BorrowMapper;
import com.libraryapi.model.Book;
import com.libraryapi.model.Borrow;
import com.libraryapi.model.BorrowStatus;
import com.libraryapi.model.Member;
import com.libraryapi.model.MemberType;
import com.libraryapi.repo.BorrowRepo;
import com.libraryapi.service.BorrowStatusService;
import com.libraryapi.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// Rule-by-rule unit tests: the repositories and the helper are mocked, so each
// test fails for exactly one reason and no database is involved.
// MemberType and BorrowStatus are entities now, so the tests build lookup rows
// and the service resolves the status by name through BorrowStatusService.
@ExtendWith(MockitoExtension.class)
class BorrowServiceImplTest {

    @Mock
    private BorrowRepo borrowRepo;

    @Mock
    private BorrowMapper borrowMapper;

    @Mock
    private BorrowHelper borrowHelper;

    @Mock
    private BorrowStatusService borrowStatusService;

    @Mock
    private MemberService memberService;

    private BorrowServiceImpl service;

    private final LocalDate today = LocalDate.now();

    // lookup rows standing in for the seeded LB_MEMBER_TYPE rows
    private final MemberType studentType = memberType("STUDENT", 14, 3);
    private final MemberType normalType = memberType("NORMAL", 14, null);
    private final MemberType premiumType = memberType("PREMIUM", 30, null);

    // lookup rows standing in for the seeded LB_BORROW_STATUS rows
    private final BorrowStatus borrowedStatus = status(1L, "BORROWED");
    private final BorrowStatus overdueStatus = status(2L, "OVERDUE");
    private final BorrowStatus returnedStatus = status(3L, "RETURNED");

    @BeforeEach
    void setUp() {
        LibraryProperties properties = new LibraryProperties();
        properties.setFinePerDay(new BigDecimal("5.00"));

        service = new BorrowServiceImpl(
                borrowRepo, borrowMapper, borrowHelper, properties,
                borrowStatusService, memberService);

        // The service resolves the status names from the lookup table. lenient()
        // keeps strict-stubbing quiet: a rejection test never reaches some names.
        lenient().when(borrowStatusService.getBorrowStatusEntityByName("BORROWED"))
                .thenReturn(borrowedStatus);
        lenient().when(borrowStatusService.getBorrowStatusEntityByName("OVERDUE"))
                .thenReturn(overdueStatus);
        lenient().when(borrowStatusService.getBorrowStatusEntityByName("RETURNED"))
                .thenReturn(returnedStatus);
    }

    // ---------- rule 3: loan period comes from the member type ----------

    @Test
    void studentBorrowsForFourteenDaysAndTakesACopy() {
        Book book = book(2);
        stubBookAndMember(book, member(studentType));
        stubSuccessfulSave();

        service.borrow(request());

        Borrow saved = captureSavedBorrow();

        assertThat(saved.getBorrowDate()).isEqualTo(today);
        assertThat(saved.getDueDate()).isEqualTo(today.plusDays(14));
        assertThat(saved.getBook().getAvailableCopies()).isEqualTo(1);
        assertThat(saved.getReturnDate()).isNull();
        assertThat(saved.getFine()).isNull();
        // stored state of a fresh borrow
        assertThat(saved.getStatus()).isEqualTo(borrowedStatus);
        assertThat(saved.getOverdueDays()).isZero();
    }

    @Test
    void premiumBorrowsForThirtyDays() {
        Book book = book(1);
        stubBookAndMember(book, member(premiumType));
        stubSuccessfulSave();

        service.borrow(request());

        assertThat(captureSavedBorrow().getDueDate()).isEqualTo(today.plusDays(30));
    }

    @Test
    void borrowingRefreshesTheStoredOverdueFlagsFirst() {
        Book book = book(1);
        stubBookAndMember(book, member(normalType));
        stubSuccessfulSave();

        service.borrow(request());

        // rule 4 reads the stored status, so the refresh must run first
        verify(borrowHelper).refreshOverdue();
    }

    // ---------- rule 1: a copy must be available ----------

    @Test
    void rejectsBorrowWhenNoCopiesAreAvailable() {
        Book book = book(0);
        when(memberService.getMemberEntityById(2L)).thenReturn(member(normalType));
        when(borrowHelper.lockBookOrThrow(1L)).thenReturn(book);

        assertThatThrownBy(() -> service.borrow(request()))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("No copies");

        assertThat(book.getAvailableCopies()).isZero();
        verify(borrowRepo, never()).save(any());
    }

    // ---------- rule 2: the type's active-borrow cap ----------

    @Test
    void studentHoldingThreeActiveBorrowsCannotTakeAFourth() {
        Member student = member(studentType);
        when(memberService.getMemberEntityById(2L)).thenReturn(student);
        when(borrowRepo.countByMemberIdAndReturnDateIsNull(2L)).thenReturn(3L);

        assertThatThrownBy(() -> service.borrow(request()))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("at most 3");

        // rejected before any book row is locked or a copy is taken
        verify(borrowHelper, never()).lockBookOrThrow(anyLong());
        verify(borrowRepo, never()).save(any());
    }

    @Test
    void studentWithTwoActiveBorrowsCanTakeTheThird() {
        Book book = book(1);
        stubBookAndMember(book, member(studentType));
        when(borrowRepo.countByMemberIdAndReturnDateIsNull(2L)).thenReturn(2L);
        stubSuccessfulSave();

        service.borrow(request());

        verify(borrowRepo).save(any());
    }

    @Test
    void normalMemberIsNotCapped() {
        Book book = book(20);
        stubBookAndMember(book, member(normalType));
        stubSuccessfulSave();

        service.borrow(request());

        // the cap query is not even executed for types without a limit
        verify(borrowRepo, never()).countByMemberIdAndReturnDateIsNull(anyLong());
        verify(borrowRepo).save(any());
    }

    // ---------- rule 4: overdue books block new borrows ----------

    @Test
    void memberWithAnOverdueBookCannotBorrow() {
        Book book = book(3);
        when(memberService.getMemberEntityById(2L)).thenReturn(member(studentType));
        when(borrowRepo.existsByMemberIdAndStatus(2L, overdueStatus)).thenReturn(true);

        assertThatThrownBy(() -> service.borrow(request()))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("overdue");

        assertThat(book.getAvailableCopies()).isEqualTo(3);
        verify(borrowHelper, never()).lockBookOrThrow(anyLong());
        verify(borrowRepo, never()).save(any());
    }

    // ---------- rules 5 and 6: returning late produces a fine ----------

    @Test
    void returningOnTimeHasNoFineAndPutsTheCopyBack() {
        Book book = book(0);
        Borrow borrow = openBorrow(book, today);

        when(borrowHelper.lockBorrowOrThrow(5L)).thenReturn(borrow);

        service.returnBook(5L);

        assertThat(borrow.getReturnDate()).isEqualTo(today);
        assertThat(borrow.getFine()).isEqualByComparingTo("0.00");
        assertThat(borrow.getStatus()).isEqualTo(returnedStatus);
        assertThat(borrow.getOverdueDays()).isZero();
        assertThat(book.getAvailableCopies()).isEqualTo(1);
    }

    @Test
    void returningFiveDaysLateChargesFiveDays() {
        Book book = book(0);
        Borrow borrow = openBorrow(book, today.minusDays(5));

        when(borrowHelper.lockBorrowOrThrow(5L)).thenReturn(borrow);

        service.returnBook(5L);

        // 5 days * 5.00 per day
        assertThat(borrow.getFine()).isEqualByComparingTo("25.00");
        assertThat(borrow.getStatus()).isEqualTo(returnedStatus);
        assertThat(borrow.getOverdueDays()).isEqualTo(5);
        assertThat(book.getAvailableCopies()).isEqualTo(1);
    }

    @Test
    void fineUsesTheConfiguredDailyRate() {
        LibraryProperties cheap = new LibraryProperties();
        cheap.setFinePerDay(new BigDecimal("2.50"));
        BorrowServiceImpl cheapService = new BorrowServiceImpl(
                borrowRepo, borrowMapper, borrowHelper, cheap,
                borrowStatusService, memberService);

        Book book = book(0);
        Borrow borrow = openBorrow(book, today.minusDays(3));

        when(borrowHelper.lockBorrowOrThrow(5L)).thenReturn(borrow);

        cheapService.returnBook(5L);

        assertThat(borrow.getFine()).isEqualByComparingTo("7.50");
    }

    @Test
    void returningTheSameBorrowTwiceIsRejected() {
        Book book = book(1);
        Borrow borrow = openBorrow(book, today);
        borrow.setReturnDate(today.minusDays(1));

        when(borrowHelper.lockBorrowOrThrow(5L)).thenReturn(borrow);

        assertThatThrownBy(() -> service.returnBook(5L))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("already returned");

        // the copy is not put back a second time
        assertThat(book.getAvailableCopies()).isEqualTo(1);
    }

    // ---------- stored status / overdue days ----------

    @Test
    void overdueListingRefreshesFirstThenReadsTheStoredFlag() {
        Borrow borrow = openBorrow(book(0), today.minusDays(3));

        when(borrowRepo.findByStatus(overdueStatus)).thenReturn(List.of(borrow));
        when(borrowMapper.convertFromBorrowListToBorrowDtoList(any()))
                .thenReturn(List.of(new BorrowDTO()));

        List<BorrowDTO> result = service.getOverdueBorrows();

        // refreshOverdue (the helper, covered by the integration tests) runs
        // before the status column is trusted for the read.
        assertThat(result).hasSize(1);
        verify(borrowHelper).refreshOverdue();
        verify(borrowRepo).findByStatus(overdueStatus);
    }

    // ---------- lookups ----------

    @Test
    void unknownMemberIsANotFound() {
        when(memberService.getMemberEntityById(2L))
                .thenThrow(new NotFoundException("Member with id 2 not found"));

        assertThatThrownBy(() -> service.borrow(request()))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Member with id 2 not found");

        verify(borrowHelper, never()).lockBookOrThrow(anyLong());
        verify(borrowRepo, never()).save(any());
    }

    @Test
    void unknownBookIsANotFound() {
        when(memberService.getMemberEntityById(2L)).thenReturn(member(normalType));
        when(borrowHelper.lockBookOrThrow(1L))
                .thenThrow(new NotFoundException("Book with id 1 not found"));

        assertThatThrownBy(() -> service.borrow(request()))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Book with id 1 not found");

        verify(borrowRepo, never()).save(any());
    }

    @Test
    void returningAnUnknownBorrowIsANotFound() {
        when(borrowHelper.lockBorrowOrThrow(5L))
                .thenThrow(new NotFoundException("Borrow with id 5 not found"));

        assertThatThrownBy(() -> service.returnBook(5L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Borrow with id 5 not found");
    }

    // ---------- helpers ----------

    private void stubBookAndMember(Book book, Member member) {
        when(memberService.getMemberEntityById(2L)).thenReturn(member);
        // the happy path assumes the member has nothing overdue
        when(borrowRepo.existsByMemberIdAndStatus(2L, overdueStatus)).thenReturn(false);
        when(borrowHelper.lockBookOrThrow(1L)).thenReturn(book);
    }

    // Only stubbed where the borrow is expected to be written, so strict
    // stubbing does not flag unused mocks on the rejection tests.
    private void stubSuccessfulSave() {
        when(borrowRepo.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(borrowMapper.convertFromBorrowToBorrowDto(any())).thenReturn(new BorrowDTO());
    }

    private Borrow captureSavedBorrow() {
        ArgumentCaptor<Borrow> captor = ArgumentCaptor.forClass(Borrow.class);
        verify(borrowRepo).save(captor.capture());
        return captor.getValue();
    }

    private BorrowRequestDTO request() {
        return new BorrowRequestDTO(1L, 2L);
    }

    private Book book(int availableCopies) {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Clean Code");
        book.setAuthor("Robert C. Martin");
        book.setIsbn("ISBN-1");
        book.setTotalCopies(Math.max(availableCopies, 1));
        book.setAvailableCopies(availableCopies);
        return book;
    }

    private MemberType memberType(String name, int loanDays, Integer maxActiveBorrows) {
        MemberType type = new MemberType();
        type.setName(name);
        type.setLoanDays(loanDays);
        type.setMaxActiveBorrows(maxActiveBorrows);
        return type;
    }

    private BorrowStatus status(Long id, String name) {
        BorrowStatus borrowStatus = new BorrowStatus();
        borrowStatus.setId(id);
        borrowStatus.setName(name);
        return borrowStatus;
    }

    private Member member(MemberType type) {
        Member member = new Member();
        member.setId(2L);
        member.setName("Ali");
        member.setEmail("ali@example.com");
        member.setType(type);
        return member;
    }

    private Borrow openBorrow(Book book, LocalDate dueDate) {
        Borrow borrow = new Borrow();
        borrow.setId(5L);
        borrow.setBook(book);
        borrow.setMember(member(normalType));
        borrow.setBorrowDate(dueDate.minusDays(14));
        borrow.setDueDate(dueDate);
        borrow.setStatus(borrowedStatus);
        return borrow;
    }
}
