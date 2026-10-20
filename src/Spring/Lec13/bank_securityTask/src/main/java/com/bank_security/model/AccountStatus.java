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
public class AccountStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    // Fixed set per task: ACTIVE, BLOCKED, CLOSED.
    @NotBlank(message = "Account status name is required")
    @Pattern(regexp = "^(ACTIVE|BLOCKED|CLOSED)$", message = "Account status must be one of: ACTIVE, BLOCKED, CLOSED")
    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "status")
    private List<Account> accounts;
}
