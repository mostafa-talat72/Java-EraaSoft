package com.banktransferapi.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

// Payload for POST /accounts.
// openingBalance is the starting balance only; after creation the balance
// changes through transfers, never through this payload.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountRequestDTO {

    @NotBlank(message = "Account number is required")
    @Size(max = 20, message = "Account number cannot exceed 20 characters")
    private String accountNumber;

    @NotBlank(message = "Holder name is required")
    @Size(max = 100, message = "Holder name cannot exceed 100 characters")
    private String holderName;

    @NotNull(message = "Account type id is required")
    private Long accountTypeId;

    @NotNull(message = "Opening balance is required")
    @DecimalMin(value = "0.00", message = "Opening balance cannot be negative")
    @Digits(integer = 17, fraction = 2, message = "Opening balance must have at most 2 decimal places")
    private BigDecimal openingBalance;

    @NotNull(message = "Active flag is required")
    private Boolean active;
}
