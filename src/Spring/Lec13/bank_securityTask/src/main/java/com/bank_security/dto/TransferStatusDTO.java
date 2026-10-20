package com.bank_security.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Transfer status payload, no JPA.
// Fixed set: PENDING, APPROVED, REJECTED, COMPLETED, CANCELLED.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransferStatusDTO {

    private Long id;

    @NotBlank(message = "Transfer status name is required")
    @Pattern(regexp = "^(PENDING|APPROVED|REJECTED|COMPLETED|CANCELLED)$", message = "Transfer status must be one of: PENDING, APPROVED, REJECTED, COMPLETED, CANCELLED")
    private String name;
}
