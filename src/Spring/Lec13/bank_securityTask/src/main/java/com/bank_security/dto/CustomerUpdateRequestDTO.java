package com.bank_security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Partial-update payload: every field is optional.
// Validation only runs on non-null values, so sending nothing is valid too
// and sending a bad value fails fast with a clear message.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerUpdateRequestDTO {
    private Long id;

    private String fullName;

    @Email(message = "Invalid email format")
    private String email;

    @Pattern(regexp = "^01[0-9]{9}$", message = "Phone must be 11 digits starting with 01 (e.g. 01012345678)")
    private String phone;

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,}$",
             message = "Password must be at least 8 characters with upper, lower, digit and special char")
    private String password;
}
