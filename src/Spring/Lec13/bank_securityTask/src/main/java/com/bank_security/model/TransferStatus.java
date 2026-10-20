package com.bank_security.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransferStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    // Fixed set per task: PENDING, APPROVED, REJECTED, COMPLETED, CANCELLED.
    @NotBlank(message = "Transfer status name is required")
    @Pattern(regexp = "^(PENDING|APPROVED|REJECTED|COMPLETED|CANCELLED)$", message = "Transfer status must be one of: PENDING, APPROVED, REJECTED, COMPLETED, CANCELLED")
    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "status")
    private List<Transfer> transfers;
}
