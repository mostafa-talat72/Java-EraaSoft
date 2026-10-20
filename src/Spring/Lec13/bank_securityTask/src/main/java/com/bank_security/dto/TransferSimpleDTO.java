package com.bank_security.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

// Slim transfer view: slim/simple relations only, no cycles.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransferSimpleDTO {

    private Long id;

    @NotNull(message = "Destination account is required")
    private AccountRequestTransferDTO toAccount;

    @NotNull(message = "Transfer amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Transfer amount must be greater than 0")
    private BigDecimal amount;
}
