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

// Payload for POST /account-types and PUT /account-types/{id}.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountTypeRequestDTO {

    @NotBlank(message = "Account type name is required")
    @Size(max = 30, message = "Account type name cannot exceed 30 characters")
    private String name;

    @NotNull(message = "Daily limit is required")
    @DecimalMin(value = "0.01", message = "Daily limit must be greater than zero")
    @Digits(integer = 17, fraction = 2, message = "Daily limit must have at most 2 decimal places")
    private BigDecimal dailyLimit;
}
