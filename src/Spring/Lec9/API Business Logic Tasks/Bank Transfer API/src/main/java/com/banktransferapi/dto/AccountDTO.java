package com.banktransferapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

// Full account view — the nested type is the light SimpleDTO.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {

    private Long id;

    private String accountNumber;

    private String holderName;

    private BigDecimal balance;

    private Boolean active;

    private AccountTypeSimpleDTO accountType;
}
