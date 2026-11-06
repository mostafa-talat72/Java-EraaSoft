package com.libraryapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Payload for POST /borrow-statuses and PUT /borrow-statuses/{id}.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BorrowStatusRequestDTO {

    @NotBlank(message = "Borrow status name is required")
    @Size(max = 20, message = "Borrow status name cannot exceed 20 characters")
    private String name;
}
