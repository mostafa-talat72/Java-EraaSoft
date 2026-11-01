package com.banktransferapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "BT_TRANSFER", indexes = {
        @Index(name = "IDX_BT_TR_SENDER_DATE", columnList = "sender_id, created_at"),
        @Index(name = "IDX_BT_TR_RECEIVER", columnList = "receiver_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull(message = "Sender account is required")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sender_id", nullable = false)
    private Account sender;

    @NotNull(message = "Receiver account is required")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "receiver_id", nullable = false)
    private Account receiver;

    @NotNull(message = "Transfer amount is required")
    @DecimalMin(value = "0.01", message = "Transfer amount must be greater than zero")
    @Digits(integer = 17, fraction = 2, message = "Transfer amount must have at most 2 decimal places")
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    // No @NotNull here: Bean Validation may run before @PrePersist fills the value.
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void onCreate() {
        if (Objects.isNull(createdAt)) {
            createdAt = LocalDateTime.now();
        }
    }
}
