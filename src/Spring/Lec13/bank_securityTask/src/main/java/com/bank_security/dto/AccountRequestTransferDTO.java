package com.bank_security.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Account payload, no JPA. Owner carried as slim view (no password, no back-reference).
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountRequestTransferDTO {
    private String accountNumber;
}
