package com.libraryapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Lookup table for membership types (seeded with STUDENT, NORMAL, PREMIUM).
// The two per-type rules live on the row, so the borrowing logic never needs
// an if/else per membership type:
//   loanDays          - rule 3: 14 for STUDENT/NORMAL, 30 for PREMIUM.
//   maxActiveBorrows  - rule 2: 3 for STUDENT, null (no cap) for the others.
@Entity
@Table(name = "LB_MEMBER_TYPE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberType {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotBlank(message = "Member type name is required")
    @Size(max = 30, message = "Member type name cannot exceed 30 characters")
    @Column(nullable = false, unique = true, length = 30)
    private String name;

    @NotNull(message = "Loan days is required")
    @Min(value = 1, message = "Loan days must be at least 1")
    @Column(name = "loan_days", nullable = false)
    private Integer loanDays;

    // null means "no limit", not "zero books"
    @Min(value = 1, message = "Max active borrows must be at least 1")
    @Column(name = "max_active_borrows")
    private Integer maxActiveBorrows;
}
