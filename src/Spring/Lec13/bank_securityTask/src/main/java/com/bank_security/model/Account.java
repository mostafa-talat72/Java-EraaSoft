package com.bank_security.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    // @NotBlank removed on purpose: the number is always generated below,
    // the client must not send it (any sent value is ignored in service).
    @Column(unique = true, nullable = false)
    private String accountNumber;

    // @NotNull removed on purpose: null becomes 0 in onCreate() below,
    // and Bean Validation runs before @PrePersist callbacks.
    // BigDecimal for money (precision 15, scale 2 = safe for amounts).
    @PositiveOrZero(message = "Account balance cannot be negative")
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal balance;

    // @NotBlank removed on purpose: null becomes "EGP" in onCreate() below,
    // and Bean Validation runs before @PrePersist callbacks.
    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency must be a 3-letter code (e.g. EGP, USD)")
    @Column(nullable = false)
    private String currency;


    @NotNull(message = "Account status is required")
    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private AccountStatus status;

    // Owning side of the one-to-one with Customer: FK customer_id lives in
    // the account table, unique for a true one-to-one. Saving the account
    // links it to the customer in one shot.
    @OneToOne
    @JoinColumn(name = "customer_id", unique = true)
    private Customer customer;


    @OneToMany(mappedBy = "fromAccount")
    private List<Transfer> outgoingTransfers;

    @OneToMany(mappedBy = "toAccount")
    private List<Transfer> incomingTransfers;


    // Null sent -> default applied automatically before first save.
    // accountNumber is always generated here; any client-sent value is
    // cleared in service before mapping, so the user can never set it.
    @PrePersist
    void onCreate() {
        if (balance == null) balance = BigDecimal.ZERO;
        if (currency == null) currency = "EGP";
        if (accountNumber == null || accountNumber.isBlank()) {
            accountNumber = String.valueOf(System.currentTimeMillis());
        }
    }
}
