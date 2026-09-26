package com.lec9.employeeemailtask.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

// Employee entity (parent side of OneToMany with Email).
// Bean Validation runs before DB; @CheckConstraint is a DB-level guard.
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    // PK generated from employee_seq.
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "employee_seq"
    )
    @SequenceGenerator(
            name = "employee_seq",
            sequenceName = "employee_seq",
            allocationSize = 1
    )
    private Long id;

    @NotBlank(message = "Employee name is required")
    @Column(nullable = false)
    private String name;

    // Age: 16-39 (implements task rule age > 15 and < 40).
    @NotNull(message = "Employee age is required")
    @Min(value = 15, message = "Employee age must be greater than 15")
    @Max(value = 39, message = "Employee age must be less than 40")
    @Column(
            nullable = false,
            check = {
                    @CheckConstraint(
                            name = "CH_AGE",
                            constraint = "age > 15 AND age < 40"
                    )
            }
    )
    private Integer age;

    // Salary: strictly between 5000 and 10000 (exclusive).
    @NotNull(message = "Employee salary is required")
    @DecimalMin(
            value = "5000",
            inclusive = false,
            message = "Employee salary must be greater than 5000"
    )
    @DecimalMax(
            value = "10000",
            inclusive = false,
            message = "Employee salary must be less than 10000"
    )
    @Column(
            nullable = false,
            check = {
                    @CheckConstraint(
                            name = "CH_SALARY",
                            constraint = "salary > 5000 AND salary < 10000"
                    )
            }
    )
    private Double salary;

    // Child emails: cascade ALL + orphanRemoval so save/delete propagates.
    @OneToMany(
            mappedBy = "employee",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Email> emails = new ArrayList<>();
}