package com.bank_security.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Minimal payload for admin-driven account creation.
// Only the target customer id is required; accountNumber, balance,
// currency and status are all server-managed (defaults + ACTIVE).
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountCreateRequestDTO {

    @NotNull(message = "Customer id is required")
    private Long customerId;
}
