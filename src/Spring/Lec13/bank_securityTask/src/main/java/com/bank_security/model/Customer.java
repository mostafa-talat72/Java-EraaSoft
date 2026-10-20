package com.bank_security.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;


    @NotBlank(message = "Customer full name is required")
    @Column(nullable = false)
    private String fullName;

    @NotBlank(message = "Customer email is required")
    @Email(message = "Invalid email format")
    @Column(unique = true, nullable = false)
    private String email;

    // Stored encrypted (BCrypt) in service layer; validated raw here before encoding.
    @NotBlank(message = "Customer password is required")
    @Size(min = 8, message = "Customer password must be at least 8 characters")
    @Column(nullable = false)
    private String password;

    @NotBlank(message = "Customer phone is required")
    @Pattern(regexp = "^01[0-9]{9}$", message = "Phone must be 11 digits starting with 01 (e.g. 01012345678)")
    @Column(nullable = false)
    private String phone;

    // @NotNull removed on purpose: null is defaulted in onCreate() below,
    // and Bean Validation runs before @PrePersist callbacks.
    @Column(nullable = false)
    private Boolean enabled;

    @PastOrPresent(message = "Creation date cannot be in the future")
    private Timestamp createdAt;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "customer_role",
            joinColumns = @JoinColumn(name = "customer_id"),
            inverseJoinColumns = @JoinColumn(name =  "role_id"),
            uniqueConstraints = @UniqueConstraint(
                    columnNames = {"customer_id", "role_id"}
            )
    )
    private List<Role> roles;

    // Inverse side of the one-to-one owned by Account (FK customer_id there).
    // A customer can be created before its account exists (nullable link).
    @OneToOne(mappedBy = "customer")
    private Account account;

    // Null sent -> default applied automatically before first save.
    @PrePersist
    void onCreate() {
        if (enabled == null) enabled = true;
        if (createdAt == null) createdAt = Timestamp.valueOf(LocalDateTime.now());
        if (roles == null) roles = new ArrayList<>();
    }
}
