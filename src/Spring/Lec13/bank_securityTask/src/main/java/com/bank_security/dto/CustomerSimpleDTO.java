package com.bank_security.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Slim customer view: identifying info only, no password, no roles, no account.
// Safe to nest inside other DTOs (no back-references, no cycles).
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSimpleDTO {

    private Long id;

    @NotBlank(message = "Customer full name is required")
    private String fullName;

    @NotBlank(message = "Customer email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Customer phone is required")
    @Pattern(regexp = "^01[0-9]{9}$", message = "Phone must be 11 digits starting with 01 (e.g. 01012345678)")
    private String phone;
}
