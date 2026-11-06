package com.libraryapi;

import com.libraryapi.model.Book;
import com.libraryapi.model.Borrow;
import com.libraryapi.model.Member;
import com.libraryapi.repo.BookRepo;
import com.libraryapi.repo.BorrowRepo;
import com.libraryapi.repo.BorrowStatusRepo;
import com.libraryapi.repo.MemberRepo;
import com.libraryapi.repo.MemberTypeRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// End-to-end tests for rules 1..6 over the real HTTP layer, service,
// transactions and database (H2 in place of Oracle).
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BorrowApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepo bookRepo;

    @Autowired
    private MemberRepo memberRepo;

    @Autowired
    private BorrowRepo borrowRepo;

    @Autowired
    private MemberTypeRepo memberTypeRepo;

    @Autowired
    private BorrowStatusRepo borrowStatusRepo;

    private final LocalDate today = LocalDate.now();

    @BeforeEach
    void setUp() {
        // Borrows reference books and members, so they must go first.
        borrowRepo.deleteAll();
        bookRepo.deleteAll();
        memberRepo.deleteAll();
    }

    // ---------- rule 3: loan period ----------

    @Test
    void studentBorrowTakesACopyAndIsDueInFourteenDays() throws Exception {
        Book book = book("B-1", 2);
        Member student = member("student@example.com", "STUDENT");

        postBorrow(book.getId(), student.getId())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.book.id").value(book.getId()))
                .andExpect(jsonPath("$.member.type.name").value("STUDENT"))
                .andExpect(jsonPath("$.status.name").value("BORROWED"))
                .andExpect(jsonPath("$.overdueDays").value(0))
                .andExpect(jsonPath("$.borrowDate").value(today.toString()))
                .andExpect(jsonPath("$.dueDate").value(today.plusDays(14).toString()));

        assertThat(availableCopies(book)).isEqualTo(1);
        assertThat(borrowRepo.count()).isEqualTo(1);
    }

    @Test
    void premiumBorrowIsDueInThirtyDays() throws Exception {
        Book book = book("B-2", 1);
        Member premium = member("premium@example.com", "PREMIUM");

        postBorrow(book.getId(), premium.getId())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.dueDate").value(today.plusDays(30).toString()));
    }

    // ---------- rule 1: copies ----------

    @Test
    void borrowingFailsWhenNoCopyIsAvailable() throws Exception {
        Book book = book("B-3", 1);
        book.setAvailableCopies(0);
        bookRepo.save(book);

        Member member = member("member@example.com", "NORMAL");

        postBorrow(book.getId(), member.getId())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("No copies")));

        assertThat(availableCopies(book)).isZero();
        assertThat(borrowRepo.count()).isZero();
    }

    // ---------- rule 2: student cap ----------

    @Test
    void studentCannotBorrowMoreThanThreeBooks() throws Exception {
        Member student = member("student2@example.com", "STUDENT");
        Book[] books = {book("B-4", 1), book("B-5", 1), book("B-6", 1), book("B-7", 1)};

        for (int i = 0; i < 3; i++) {
            postBorrow(books[i].getId(), student.getId()).andExpect(status().isCreated());
        }

        postBorrow(books[3].getId(), student.getId())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("at most 3")));

        assertThat(borrowRepo.countByMemberIdAndReturnDateIsNull(student.getId())).isEqualTo(3);
        assertThat(availableCopies(books[3])).isEqualTo(1);
    }

    @Test
    void normalMemberIsNotCappedAtThreeBooks() throws Exception {
        Member member = member("normal@example.com", "NORMAL");
        Book[] books = {book("B-8", 1), book("B-9", 1), book("B-10", 1), book("B-11", 1)};

        for (Book book : books) {
            postBorrow(book.getId(), member.getId()).andExpect(status().isCreated());
        }

        assertThat(borrowRepo.countByMemberIdAndReturnDateIsNull(member.getId())).isEqualTo(4);
    }

    // ---------- rule 4: overdue blocks borrowing ----------

    @Test
    void studentWithAnOverdueBookCannotBorrow() throws Exception {
        Member student = member("student3@example.com", "STUDENT");

        // already out and one day past its due date
        Book overdueBook = book("B-12", 1);
        overdueBook.setAvailableCopies(0);
        bookRepo.save(overdueBook);
        saveOpenBorrow(overdueBook, student, today.minusDays(1));

        Book freshBook = book("B-13", 1);

        postBorrow(freshBook.getId(), student.getId())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("overdue")));

        assertThat(availableCopies(freshBook)).isEqualTo(1);
        assertThat(borrowRepo.count()).isEqualTo(1);
    }

    // ---------- rules 5 and 6: fines ----------

    @Test
    void returningFiveDaysLateChargesFiveDays() throws Exception {
        Book book = book("B-14", 1);
        book.setAvailableCopies(0);
        bookRepo.save(book);
        Member member = member("late@example.com", "NORMAL");
        Borrow late = saveOpenBorrow(book, member, today.minusDays(5));

        returnBorrow(late.getId())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status.name").value("RETURNED"))
                .andExpect(jsonPath("$.overdueDays").value(5))
                .andExpect(jsonPath("$.fine").value(25.0))
                .andExpect(jsonPath("$.returnDate").value(today.toString()));

        assertThat(availableCopies(book)).isEqualTo(1);
        // the fine and the final state are persisted, not just computed for the response
        Borrow stored = borrowRepo.findById(late.getId()).orElseThrow();
        assertThat(stored.getFine()).isEqualByComparingTo("25.00");
        assertThat(stored.getStatus().getName()).isEqualTo("RETURNED");
        assertThat(stored.getOverdueDays()).isEqualTo(5);
    }

    @Test
    void returningOnTimeHasNoFine() throws Exception {
        Book book = book("B-15", 1);
        Member member = member("ontime@example.com", "NORMAL");

        postBorrow(book.getId(), member.getId()).andExpect(status().isCreated());
        Long borrowId = borrowRepo.findAll().get(0).getId();

        returnBorrow(borrowId)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.overdueDays").value(0))
                .andExpect(jsonPath("$.fine").value(0.0));

        assertThat(availableCopies(book)).isEqualTo(1);
    }

    @Test
    void returningTheSameBorrowTwiceIsRejected() throws Exception {
        Book book = book("B-16", 1);
        Member member = member("twice@example.com", "NORMAL");

        postBorrow(book.getId(), member.getId()).andExpect(status().isCreated());
        Long borrowId = borrowRepo.findAll().get(0).getId();

        returnBorrow(borrowId).andExpect(status().isOk());
        returnBorrow(borrowId)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("already returned")));

        assertThat(availableCopies(book)).isEqualTo(1);
    }

    // ---------- listing ----------

    @Test
    void overdueEndpointListsOnlyLateBorrows() throws Exception {
        Book lateBook = book("B-17", 1);
        lateBook.setAvailableCopies(0);
        bookRepo.save(lateBook);
        Book currentBook = book("B-18", 1);
        currentBook.setAvailableCopies(0);
        bookRepo.save(currentBook);

        Member member = member("listing@example.com", "NORMAL");
        saveOpenBorrow(lateBook, member, today.minusDays(3));
        saveOpenBorrow(currentBook, member, today.plusDays(14));

        mockMvc.perform(get("/borrows/overdue"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].book.id").value(lateBook.getId()))
                .andExpect(jsonPath("$[0].overdueDays").value(3));

        // the late row is marked in the database, the on-time one is untouched
        assertThat(borrowRepo.findByBookIdOrderByIdDesc(lateBook.getId()).get(0).getStatus().getName())
                .isEqualTo("OVERDUE");
        assertThat(borrowRepo.findByBookIdOrderByIdDesc(currentBook.getId()).get(0).getStatus().getName())
                .isEqualTo("BORROWED");
    }

    @Test
    void readingTheListStoresTheOverdueStatus() throws Exception {
        Book book = book("B-22", 1);
        book.setAvailableCopies(0);
        bookRepo.save(book);
        Member member = member("stored@example.com", "NORMAL");
        Borrow late = saveOpenBorrow(book, member, today.minusDays(2));

        // saved directly, so it starts as BORROWED
        assertThat(borrowRepo.findById(late.getId()).orElseThrow().getStatus().getName())
                .isEqualTo("BORROWED");

        mockMvc.perform(get("/borrows"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status.name").value("OVERDUE"))
                .andExpect(jsonPath("$[0].overdueDays").value(2));

        // a plain read refreshed the stored columns
        Borrow stored = borrowRepo.findById(late.getId()).orElseThrow();
        assertThat(stored.getStatus().getName()).isEqualTo("OVERDUE");
        assertThat(stored.getOverdueDays()).isEqualTo(2);
    }

    @Test
    void historyIsAvailableByMemberAndByBook() throws Exception {
        Book book = book("B-19", 1);
        Member member = member("history@example.com", "STUDENT");
        postBorrow(book.getId(), member.getId()).andExpect(status().isCreated());

        mockMvc.perform(get("/borrows/member/" + member.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        mockMvc.perform(get("/borrows/book/" + book.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        Member other = member("nohistory@example.com", "NORMAL");
        mockMvc.perform(get("/borrows/member/" + other.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void emptyBorrowListIsReportedAs404() throws Exception {
        mockMvc.perform(get("/borrows"))
                .andExpect(status().isNotFound());
    }

    // ---------- referential integrity ----------

    @Test
    void deletingABookOrMemberWithBorrowHistoryIsRefused() throws Exception {
        Book book = book("B-20", 1);
        Member member = member("delete@example.com", "NORMAL");
        postBorrow(book.getId(), member.getId()).andExpect(status().isCreated());

        mockMvc.perform(delete("/books/" + book.getId()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("borrow history")));

        mockMvc.perform(delete("/members/" + member.getId()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("borrow history")));
    }

    // ---------- validation / lookups ----------

    @Test
    void unknownIdsAndInvalidBodyAreRejected() throws Exception {
        Book book = book("B-21", 1);
        Member member = member("errors@example.com", "NORMAL");

        postBorrow(999_999L, member.getId())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(containsString("Book with id 999999")));

        postBorrow(book.getId(), 999_999L)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(containsString("Member with id 999999")));

        mockMvc.perform(post("/borrows")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());

        assertThat(availableCopies(book)).isEqualTo(1);
        assertThat(borrowRepo.count()).isZero();
    }

    // ---------- helpers ----------

    private ResultActions postBorrow(Long bookId, Long memberId) throws Exception {
        return mockMvc.perform(post("/borrows")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"bookId\":" + bookId + ",\"memberId\":" + memberId + "}"));
    }

    private ResultActions returnBorrow(Long borrowId) throws Exception {
        return mockMvc.perform(put("/borrows/" + borrowId + "/return"));
    }

    private int availableCopies(Book book) {
        return bookRepo.findById(book.getId()).orElseThrow().getAvailableCopies();
    }

    private Book book(String isbn, int totalCopies) {
        Book book = new Book();
        book.setTitle("Title " + isbn);
        book.setAuthor("Author");
        book.setIsbn(isbn);
        book.setTotalCopies(totalCopies);
        book.setAvailableCopies(totalCopies);
        return bookRepo.save(book);
    }

    private Member member(String email, String typeName) {
        Member member = new Member();
        member.setName("Member " + email);
        member.setEmail(email);
        // the seeded lookup row that drives the loan period and the cap
        member.setType(memberTypeRepo.findByName(typeName).orElseThrow());
        return memberRepo.save(member);
    }

    // A borrow that is still out with the given due date. The caller is
    // responsible for taking the copy off the shelf (availableCopies = 0).
    private Borrow saveOpenBorrow(Book book, Member member, LocalDate dueDate) {
        Borrow borrow = new Borrow();
        borrow.setBook(book);
        borrow.setMember(member);
        borrow.setBorrowDate(dueDate.minusDays(14));
        borrow.setDueDate(dueDate);
        // saved directly, so it starts in the normal "still out" state
        borrow.setStatus(borrowStatusRepo.findByName("BORROWED").orElseThrow());
        return borrowRepo.save(borrow);
    }
}
