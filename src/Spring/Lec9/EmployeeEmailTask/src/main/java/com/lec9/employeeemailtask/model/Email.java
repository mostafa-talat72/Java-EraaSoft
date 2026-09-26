package com.lec9.employeeemailtask.model;

import com.lec9.employeeemailtask.validation.ValidEmailType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Email entity (child side of ManyToOne with Employee).
// name = provider type (gmail/yahoo); content = full address, unique.
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ValidEmailType
public class Email {

    // PK generated from email_seq.
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "email_seq"
    )
    @SequenceGenerator(
            name = "email_seq",
            sequenceName = "email_seq",
            allocationSize = 1
    )
    private Long id;

    @NotBlank(message = "Email type is required")
    // Provider must start with a letter, letters/numbers only.
    @Pattern(
            regexp = "^[A-Za-z][A-Za-z0-9]*$",
            message = "Email type must start with a letter and contain only letters and numbers"
    )
    @Column(
            nullable = false,
            check = {
                    @CheckConstraint(
                            name = "CH_EMAIL_TYPE",
                            constraint = "REGEXP_LIKE(name, '^[A-Za-z][A-Za-z0-9]*$')"
                    )
            }
    )
    private String name;

    @NotBlank(message = "Email content is required")
    // Standard email shape + valid domain; unique at DB level.
    @jakarta.validation.constraints.Email(
            message = "Invalid email format"
    )
    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "Email must contain a valid domain"
    )
    @Column(
            nullable = false,
            unique = true,
            check = {
                    @CheckConstraint(
                            name = "CH_EMAIL_CONTENT",
                            constraint = "REGEXP_LIKE(content, '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$')"
                    )
            }
    )
    private String content;

    // Owning side; FK column employee_id (nullable, set by service).
    @ManyToOne
    private Employee employee;
}