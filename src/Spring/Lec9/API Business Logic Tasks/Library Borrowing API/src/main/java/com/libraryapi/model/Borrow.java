package com.libraryapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "LB_BORROW", indexes = {
        @Index(name = "IDX_LB_BORROW_MEMBER", columnList = "member_id"),
        @Index(name = "IDX_LB_BORROW_BOOK", columnList = "book_id"),
        // rule 4 asks for "still out and already due" on every borrow attempt
        @Index(name = "IDX_LB_BORROW_DUE", columnList = "return_date, due_date")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Borrow {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull(message = "Book is required")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @NotNull(message = "Member is required")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @NotNull(message = "Borrow date is required")
    @Column(name = "borrow_date", nullable = false)
    private LocalDate borrowDate;

    // borrowDate + the member type's loan period (rule 3)
    @NotNull(message = "Due date is required")
    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    // null while the book is still out
    @Column(name = "return_date")
    private LocalDate returnDate;

    // null until returned, then 0.00 when on time or days * finePerDay when late
    @Column(precision = 19, scale = 2)
    private BigDecimal fine;

    // FK to LB_BORROW_STATUS. Stored, not derived: every write path keeps it in sync.
    // BORROWED while it is out and on time, OVERDUE once the due date passes,
    // RETURNED forever after returnBook().
    @NotNull(message = "Borrow status is required")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "status_id", nullable = false)
    private BorrowStatus status;

    // Stored too: 0 while on time, then the late days once the due date passes
    // and the final value (due date -> return date) after the return.
    @Column(name = "overdue_days", nullable = false)
    private int overdueDays;
}
