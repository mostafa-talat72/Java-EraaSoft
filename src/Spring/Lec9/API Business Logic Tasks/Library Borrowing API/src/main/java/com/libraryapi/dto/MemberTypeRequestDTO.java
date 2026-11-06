package com.libraryapi.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Payload for POST /member-types and PUT /member-types/{id}.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberTypeRequestDTO {

    @NotBlank(message = "Member type name is required")
    @Size(max = 30, message = "Member type name cannot exceed 30 characters")
    private String name;

    @NotNull(message = "Loan days is required")
    @Min(value = 1, message = "Loan days must be at least 1")
    private Integer loanDays;

    // Optional: null keeps the type uncapped.
    @Min(value = 1, message = "Max active borrows must be at least 1")
    private Integer maxActiveBorrows;
}
