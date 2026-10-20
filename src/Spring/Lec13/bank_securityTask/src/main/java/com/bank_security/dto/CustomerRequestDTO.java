package com.bank_security.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

// Customer payload, no JPA. Password is validated raw here and encrypted in service layer.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRequestDTO {

    private Long id;

    @NotBlank(message = "Customer full name is required")
    private String fullName;

    @NotBlank(message = "Customer email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Customer password is required")
    @Size(min = 8, message = "Customer password must be at least 8 characters")
    private String password;

    @NotBlank(message = "Customer phone is required")
    @Pattern(regexp = "^01[0-9]{9}$", message = "Phone must be 11 digits starting with 01 (e.g. 01012345678)")
    private String phone;

}
