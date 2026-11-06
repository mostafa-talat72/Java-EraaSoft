package com.libraryapi;

import com.libraryapi.model.Book;
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

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Books and members are the two things the borrowing rules point at,
// so their CRUD is tested on its own, including the validation layer.
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class LibraryApiIntegrationTest {

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

    @BeforeEach
    void setUp() {
        borrowRepo.deleteAll();
        bookRepo.deleteAll();
        memberRepo.deleteAll();
    }

    // ---------- books ----------

    @Test
    void booksListIs404WhenEmpty() throws Exception {
        mockMvc.perform(get("/books")).andExpect(status().isNotFound());
    }

    @Test
    void createBookStartsWithEveryCopyAvailable() throws Exception {
        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookBody("Clean Code", "ISBN-100", 3)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("Clean Code"))
                .andExpect(jsonPath("$.totalCopies").value(3))
                .andExpect(jsonPath("$.availableCopies").value(3));
    }

    @Test
    void duplicateIsbnIsRejected() throws Exception {
        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookBody("Clean Code", "ISBN-101", 1)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookBody("Another Title", "ISBN-101", 1)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("already exists")));
    }

    @Test
    void invalidBookIsRejectedByBeanValidation() throws Exception {
        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"\",\"author\":\"A\",\"isbn\":\"ISBN-102\",\"totalCopies\":0}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("title")))
                .andExpect(jsonPath("$.message").value(containsString("at least one copy")));
    }

    @Test
    void getBookByIdAndMissingBook() throws Exception {
        Book book = book("ISBN-103", 2);

        mockMvc.perform(get("/books/" + book.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isbn").value("ISBN-103"));

        mockMvc.perform(get("/books/999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(containsString("Book with id 999999")));

        mockMvc.perform(get("/books/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shrinkingTheShelfBelowTheBorrowedCopiesIsRejected() throws Exception {
        // 3 copies on the shelf, 1 of them currently borrowed -> 2 available
        Book book = book("ISBN-104", 3);
        book.setAvailableCopies(1);
        bookRepo.save(book);

        mockMvc.perform(put("/books/" + book.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookBody("Clean Code", "ISBN-104", 1)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("2 copies currently borrowed")));

        // growing the shelf keeps the borrowed copy out of availability
        mockMvc.perform(put("/books/" + book.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookBody("Clean Code", "ISBN-104", 5)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCopies").value(5))
                .andExpect(jsonPath("$.availableCopies").value(3));
    }

    @Test
    void updatingABookToAnotherBooksIsbnIsRejected() throws Exception {
        book("ISBN-105", 1);
        Book second = book("ISBN-106", 1);

        mockMvc.perform(put("/books/" + second.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookBody("Second", "ISBN-105", 1)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("already exists")));
    }

    @Test
    void deleteBookReturns204Then404() throws Exception {
        Book book = book("ISBN-107", 1);

        mockMvc.perform(delete("/books/" + book.getId())).andExpect(status().isNoContent());
        mockMvc.perform(delete("/books/" + book.getId())).andExpect(status().isNotFound());
        assertThat(bookRepo.count()).isZero();
    }

    // ---------- members ----------

    @Test
    void membersListIs404WhenEmpty() throws Exception {
        mockMvc.perform(get("/members")).andExpect(status().isNotFound());
    }

    @Test
    void createMemberAndReadItBack() throws Exception {
        mockMvc.perform(post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(memberBody("Sara", "sara@example.com", memberTypeId("PREMIUM"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.type.name").value("PREMIUM"));

        mockMvc.perform(get("/members"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void duplicateEmailIsRejected() throws Exception {
        mockMvc.perform(post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(memberBody("Sara", "sara2@example.com", memberTypeId("NORMAL"))))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(memberBody("Other", "sara2@example.com", memberTypeId("STUDENT"))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("already exists")));
    }

    @Test
    void invalidMemberPayloadsAreRejected() throws Exception {
        // invalid email + blank name -> Bean Validation
        mockMvc.perform(post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(memberBody("", "not-an-email", memberTypeId("NORMAL"))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("name")))
                .andExpect(jsonPath("$.message").value(containsString("email must be valid")));

        // unknown membership type -> 404 from the lookup
        mockMvc.perform(post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(memberBody("Ali", "ali2@example.com", 999_999L)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(containsString("Member type with id 999999")));

        // missing typeId -> Bean Validation
        mockMvc.perform(post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Ali\",\"email\":\"ali3@example.com\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Member type id is required")));
    }

    @Test
    void updateMemberChangesTheTypeAndDeleteWorks() throws Exception {
        Member member = member("upd@example.com", "NORMAL");

        mockMvc.perform(put("/members/" + member.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(memberBody("Upgraded", "upd@example.com", memberTypeId("PREMIUM"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Upgraded"))
                .andExpect(jsonPath("$.type.name").value("PREMIUM"));

        mockMvc.perform(delete("/members/" + member.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/members/" + member.getId()))
                .andExpect(status().isNotFound());
    }

    // ---------- lookup tables (member types / borrow statuses) ----------

    @Test
    void seededLookupTablesAreExposed() throws Exception {
        mockMvc.perform(get("/member-types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].name").value("STUDENT"))
                .andExpect(jsonPath("$[0].loanDays").value(14))
                .andExpect(jsonPath("$[0].maxActiveBorrows").value(3))
                .andExpect(jsonPath("$[2].name").value("PREMIUM"))
                .andExpect(jsonPath("$[2].loanDays").value(30));

        mockMvc.perform(get("/borrow-statuses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].name").value("BORROWED"))
                .andExpect(jsonPath("$[1].name").value("OVERDUE"))
                .andExpect(jsonPath("$[2].name").value("RETURNED"));
    }

    @Test
    void memberTypeCrudAndDeleteGuard() throws Exception {
        String goldBody = "{\"name\":\"GOLD\",\"loanDays\":21,\"maxActiveBorrows\":4}";

        mockMvc.perform(post("/member-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(goldBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("GOLD"))
                .andExpect(jsonPath("$.loanDays").value(21))
                .andExpect(jsonPath("$.maxActiveBorrows").value(4));

        Long goldId = memberTypeRepo.findByName("GOLD").orElseThrow().getId();

        mockMvc.perform(put("/member-types/" + goldId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"GOLD\",\"loanDays\":28,\"maxActiveBorrows\":5}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.loanDays").value(28))
                .andExpect(jsonPath("$.maxActiveBorrows").value(5));

        // duplicate name is a clear 400, not a unique-constraint 500
        mockMvc.perform(post("/member-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(goldBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("already exists")));

        // a type still assigned to a member cannot be deleted
        mockMvc.perform(post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(memberBody("Gold Member", "gold@example.com", goldId)))
                .andExpect(status().isCreated());

        mockMvc.perform(delete("/member-types/" + goldId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("used by existing members")));

        // once the member is gone the type can be deleted
        Member goldMember = memberRepo.findAll().get(0);
        mockMvc.perform(delete("/members/" + goldMember.getId()))
                .andExpect(status().isNoContent());
        mockMvc.perform(delete("/member-types/" + goldId))
                .andExpect(status().isNoContent());
        mockMvc.perform(get("/member-types/" + goldId))
                .andExpect(status().isNotFound());
    }

    @Test
    void borrowStatusCrudWorks() throws Exception {
        mockMvc.perform(post("/borrow-statuses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"LOST\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("LOST"));

        Long lostId = borrowStatusRepo.findByName("LOST").orElseThrow().getId();

        mockMvc.perform(post("/borrow-statuses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"LOST\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("already exists")));

        mockMvc.perform(put("/borrow-statuses/" + lostId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"MISSING\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("MISSING"));

        // unused by any borrow, so it can be removed
        mockMvc.perform(delete("/borrow-statuses/" + lostId))
                .andExpect(status().isNoContent());
        mockMvc.perform(get("/borrow-statuses/" + lostId))
                .andExpect(status().isNotFound());
    }

    // ---------- helpers ----------

    private String bookBody(String title, String isbn, int totalCopies) {
        return "{\"title\":\"" + title + "\",\"author\":\"Author\",\"isbn\":\"" + isbn
                + "\",\"totalCopies\":" + totalCopies + "}";
    }

    private String memberBody(String name, String email, Long typeId) {
        return "{\"name\":\"" + name + "\",\"email\":\"" + email + "\",\"typeId\":" + typeId + "}";
    }

    private Long memberTypeId(String name) {
        return memberTypeRepo.findByName(name).orElseThrow().getId();
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
        member.setType(memberTypeRepo.findByName(typeName).orElseThrow());
        return memberRepo.save(member);
    }
}
