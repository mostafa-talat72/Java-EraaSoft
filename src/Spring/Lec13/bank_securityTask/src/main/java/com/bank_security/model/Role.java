package com.bank_security.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    // Fixed set per task: ADMIN, BANK_MANAGER, CUSTOMER, AUDITOR.
    @NotBlank(message = "Role name is required")
    @Pattern(regexp = "^(ADMIN|BANK_MANAGER|CUSTOMER|AUDITOR)$", message = "Role must be one of: ADMIN, BANK_MANAGER, CUSTOMER, AUDITOR")
    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "roles")
    private List<Customer> customers;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "role_permission",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"),
            uniqueConstraints = @UniqueConstraint(
                    columnNames = {"role_id", "permission_id"}
            )
    )
    private List<Permission> permissions;

    // Null sent -> default applied automatically before first save.
    @PrePersist
    void onCreate() {
        if (permissions == null) permissions = new ArrayList<>();
    }
}
