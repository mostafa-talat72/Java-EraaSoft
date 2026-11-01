package com.banktransferapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

// Light account reference — nested inside TransferDTO.
// balance is the value at response time (after the transfer for POST /transfers).
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountSimpleDTO {

    private Long id;

    private String accountNumber;

    private String holderName;

    private BigDecimal balance;
}
