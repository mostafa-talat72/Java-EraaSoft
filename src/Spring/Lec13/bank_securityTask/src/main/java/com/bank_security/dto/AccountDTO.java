package com.bank_security.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

// Account payload, no JPA. Owner carried as slim view (no password, no back-reference).
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {

    private Long id;

    // No validation here on purpose: the number is generated server-side
    // (Account.onCreate) and any client-sent value is ignored in service.
    // Present in DTO for responses only.
    private String accountNumber;

    // No @NotNull/@NotBlank here on purpose: null passes through and the
    // entity default applies (balance -> 0, currency -> EGP).
    @PositiveOrZero(message = "Account balance cannot be negative")
    private BigDecimal balance;

    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency must be a 3-letter code (e.g. EGP, USD)")
    private String currency;

    @Valid
    @NotNull(message = "Account status is required")
    private AccountStatusDTO status;

    @Valid
    @NotNull(message = "Account owner is required")
    private CustomerSimpleDTO customer;
}
