package com.banktransferapi.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

// Payload for POST /transfers.
// Above the configured threshold the transfer is two-step:
// the first attempt (no code) sends a code, the retry carries it here.
// The conditional rule lives in the service, not in Bean Validation.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequestDTO {

    @NotNull(message = "Sender account id is required")
    private Long senderAccountId;

    @NotNull(message = "Receiver account id is required")
    private Long receiverAccountId;

    @NotNull(message = "Transfer amount is required")
    @DecimalMin(value = "0.01", message = "Transfer amount must be greater than zero")
    @Digits(integer = 17, fraction = 2, message = "Transfer amount must have at most 2 decimal places")
    private BigDecimal amount;

    // Optional at binding time: required only above the threshold, and that
    // conditional rule lives in the service, not in Bean Validation.
    @Pattern(regexp = "\\d{6}", message = "Verification code must be exactly 6 digits")
    private String verificationCode;
}
