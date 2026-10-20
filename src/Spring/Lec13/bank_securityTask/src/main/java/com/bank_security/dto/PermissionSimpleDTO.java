package com.bank_security.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Slim permission view: id + name only (already a leaf, same shape as PermissionDTO).
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PermissionSimpleDTO {

    private Long id;

    @NotBlank(message = "Permission name is required")
    @Pattern(regexp = "^(ACCOUNT_READ|ACCOUNT_CREATE|ACCOUNT_UPDATE|ACCOUNT_DELETE|TRANSFER_CREATE|TRANSFER_READ|TRANSFER_APPROVE|CUSTOMER_READ|CUSTOMER_UPDATE|REPORT_VIEW)$", message = "Unknown permission name")
    private String name;
}
