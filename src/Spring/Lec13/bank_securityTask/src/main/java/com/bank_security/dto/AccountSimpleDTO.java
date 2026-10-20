package com.bank_security.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

// Slim account view: no owner back-reference, safe to nest (no cycles).
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountSimpleDTO {

    private Long id;

    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency must be a 3-letter code (e.g. EGP, USD)")
    private String currency;

    // Optional top-up field: managers/admins can adjust the balance.
    @PositiveOrZero(message = "Account balance cannot be negative")
    private BigDecimal balance;

    @Valid
    @NotNull(message = "Account status is required")
    private AccountStatusDTO status;
}
