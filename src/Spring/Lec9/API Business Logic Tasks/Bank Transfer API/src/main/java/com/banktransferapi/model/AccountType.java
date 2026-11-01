package com.banktransferapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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

@Entity
@Table(name = "BT_ACCOUNT_TYPE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountType {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotBlank(message = "Account type name is required")
    @Size(max = 30, message = "Account type name cannot exceed 30 characters")
    @Column(nullable = false, unique = true, length = 30)
    private String name;

    @NotNull(message = "Daily limit is required")
    @DecimalMin(value = "0.01", message = "Daily limit must be greater than zero")
    @Digits(integer = 17, fraction = 2, message = "Daily limit must have at most 2 decimal places")
    @Column(name = "daily_limit", nullable = false, precision = 19, scale = 2)
    private BigDecimal dailyLimit;
}
