package com.bank_security.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

// Transfer payload, no JPA. Relations carried as slim views (no cycles, no password leak).
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransferDTO {

    private Long id;

    @Valid
    @NotNull(message = "Source account is required")
    private AccountRequestTransferDTO fromAccount;

    @Valid
    @NotNull(message = "Destination account is required")
    private AccountRequestTransferDTO toAccount;

    @NotNull(message = "Transfer amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Transfer amount must be greater than 0")
    private BigDecimal amount;

    @Valid
    @NotNull(message = "Transfer status is required")
    private TransferStatusDTO status;

    @PastOrPresent(message = "Creation date cannot be in the future")
    private Timestamp createdAt;

    @Valid
    @NotNull(message = "Transfer creator is required")
    private CustomerSimpleDTO createdBy;
}
