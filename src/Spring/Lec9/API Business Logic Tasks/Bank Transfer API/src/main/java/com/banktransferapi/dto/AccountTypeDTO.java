package com.banktransferapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

// Full account type view.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountTypeDTO {

    private Long id;

    private String name;

    private BigDecimal dailyLimit;
}
