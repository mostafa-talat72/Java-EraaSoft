package com.libraryapi.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Payload for POST /borrows: who takes which book.
// Dates are never sent by the client: borrowDate is today and dueDate comes
// from the member type (rule 3), so they cannot be forged.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BorrowRequestDTO {

    @NotNull(message = "Book id is required")
    private Long bookId;

    @NotNull(message = "Member id is required")
    private Long memberId;
}
