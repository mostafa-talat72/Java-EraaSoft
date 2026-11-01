package com.banktransferapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// Full transfer view — the nested accounts carry their balance at response time.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransferDTO {

    private Long id;

    private AccountSimpleDTO sender;

    private AccountSimpleDTO receiver;

    private BigDecimal amount;

    private LocalDateTime createdAt;
}
