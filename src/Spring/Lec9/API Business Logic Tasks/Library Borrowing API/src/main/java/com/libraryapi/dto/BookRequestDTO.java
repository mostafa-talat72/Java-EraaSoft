package com.libraryapi.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Payload for POST /books and PUT /books/{id}.
// totalCopies is the shelf size; availableCopies is derived by the service.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookRequestDTO {

    @NotBlank(message = "Book title is required")
    @Size(max = 150, message = "Book title cannot exceed 150 characters")
    private String title;

    @NotBlank(message = "Book author is required")
    @Size(max = 100, message = "Book author cannot exceed 100 characters")
    private String author;

    @NotBlank(message = "Book isbn is required")
    @Size(max = 20, message = "Book isbn cannot exceed 20 characters")
    private String isbn;

    @NotNull(message = "Total copies is required")
    @Min(value = 1, message = "A book must have at least one copy")
    private Integer totalCopies;
}
