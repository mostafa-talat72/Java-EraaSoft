package com.bank_security.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Slim account-status view (already a leaf, same shape as AccountStatusDTO).
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountStatusSimpleDTO {

    private Long id;

    @NotBlank(message = "Account status name is required")
    @Pattern(regexp = "^(ACTIVE|BLOCKED|CLOSED)$", message = "Account status must be one of: ACTIVE, BLOCKED, CLOSED")
    private String name;
}
