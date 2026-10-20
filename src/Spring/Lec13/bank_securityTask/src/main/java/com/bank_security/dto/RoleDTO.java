package com.bank_security.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

// Role payload, no JPA. Fixed set per task: ADMIN, BANK_MANAGER, CUSTOMER, AUDITOR.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO {

    private Long id;

    @NotBlank(message = "Role name is required")
    @Pattern(regexp = "^(ADMIN|BANK_MANAGER|CUSTOMER|AUDITOR)$", message = "Role must be one of: ADMIN, BANK_MANAGER, CUSTOMER, AUDITOR")
    private String name;

    @Valid
    private List<PermissionDTO> permissions = new ArrayList<>();
}
