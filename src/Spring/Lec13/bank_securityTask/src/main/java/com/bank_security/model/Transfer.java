package com.bank_security.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull(message = "Source account is required")
    @ManyToOne
    @JoinColumn(name = "from_account_id", nullable = false)
    private Account fromAccount;

    @NotNull(message = "Destination account is required")
    @ManyToOne
    @JoinColumn(name = "to_account_id", nullable = false)
    private Account toAccount;

    @NotNull(message = "Transfer amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Transfer amount must be greater than 0")
    @Column(nullable = false)
    private BigDecimal amount;

    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency must be a 3-letter code (e.g. EGP, USD)")
    private String currency;

    @NotNull(message = "Transfer status is required")
    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private TransferStatus status;

    @PastOrPresent(message = "Creation date cannot be in the future")
    private Timestamp createdAt;

    @NotNull(message = "Transfer creator is required")
    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private Customer createdBy;

    // Null sent -> default applied automatically before first save.
    @PrePersist
    void onCreate() {
        if (createdAt == null) createdAt = Timestamp.valueOf(LocalDateTime.now());
        if (currency == null) currency = "EGP";
    }
}