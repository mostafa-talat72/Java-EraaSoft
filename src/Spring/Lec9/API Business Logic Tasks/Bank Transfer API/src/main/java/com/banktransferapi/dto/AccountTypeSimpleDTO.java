package com.banktransferapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

// Nested inside AccountDTO — keeps the response light (no accounts list).
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountTypeSimpleDTO {

    private Long id;

    private String name;

    private BigDecimal dailyLimit;
}
