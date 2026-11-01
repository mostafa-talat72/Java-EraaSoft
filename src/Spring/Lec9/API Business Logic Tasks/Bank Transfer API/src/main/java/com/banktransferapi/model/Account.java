package com.banktransferapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
import java.util.List;

@Entity
@Table(name = "BT_ACCOUNT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotBlank(message = "Account number is required")
    @Size(max = 20, message = "Account number cannot exceed 20 characters")
    @Column(name = "account_number", nullable = false, unique = true, length = 20)
    private String accountNumber;

    @NotBlank(message = "Holder name is required")
    @Size(max = 100, message = "Holder name cannot exceed 100 characters")
    @Column(name = "holder_name", nullable = false, length = 100)
    private String holderName;

    @NotNull(message = "Balance is required")
    @DecimalMin(value = "0.00", message = "Balance cannot be negative")
    @Digits(integer = 17, fraction = 2, message = "Balance must have at most 2 decimal places")
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;

    @NotNull(message = "Active flag is required")
    @Column(nullable = false)
    private Boolean active;

    @NotNull(message = "Account type is required")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_type_id", nullable = false)
    private AccountType accountType;

    // Inverse side of the relation: the foreign key lives in Transfer.sender,
    // so there is no @JoinColumn here (that would be ignored and is misleading).
    @OneToMany(mappedBy = "sender", fetch = FetchType.LAZY)
    private List<Transfer> transfersSent;

    @OneToMany(mappedBy = "receiver", fetch = FetchType.LAZY)
    private List<Transfer> transfersRecipient;
}
