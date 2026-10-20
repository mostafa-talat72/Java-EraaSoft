package com.bank_security.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Slim role view: id + name only, no permissions back-reference.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleSimpleDTO {

    private Long id;

    @NotBlank(message = "Role name is required")
    @Pattern(regexp = "^(ADMIN|BANK_MANAGER|CUSTOMER|AUDITOR)$", message = "Role must be one of: ADMIN, BANK_MANAGER, CUSTOMER, AUDITOR")
    private String name;
}
