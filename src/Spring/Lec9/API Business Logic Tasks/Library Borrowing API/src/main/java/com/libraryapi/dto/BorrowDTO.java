package com.libraryapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

// Full borrow view.
// status and overdueDays are stored on the entity and kept current by the
// service; fine is stored when the book comes back.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BorrowDTO {

    private Long id;

    private BookSimpleDTO book;

    private MemberSimpleDTO member;

    private LocalDate borrowDate;

    private LocalDate dueDate;

    private LocalDate returnDate;

    private BorrowStatusDTO status;

    private long overdueDays;

    private BigDecimal fine;
}
