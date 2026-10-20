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
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    // Fixed set per task: ACCOUNT_READ/CREATE/UPDATE/DELETE, TRANSFER_CREATE/READ/APPROVE, CUSTOMER_READ/UPDATE, REPORT_VIEW.
    @NotBlank(message = "Permission name is required")
    @Pattern(regexp = "^(ACCOUNT_READ|ACCOUNT_CREATE|ACCOUNT_UPDATE|ACCOUNT_DELETE|TRANSFER_CREATE|TRANSFER_READ|TRANSFER_APPROVE|CUSTOMER_READ|CUSTOMER_UPDATE|REPORT_VIEW)$", message = "Unknown permission name")
    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "permissions")
    private List<Role> roles;
}
