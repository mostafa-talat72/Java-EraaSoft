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
public class CustomerResponseMeDTO {

    private Long id;

    @NotBlank(message = "Customer full name is required")
    private String fullName;

    @NotBlank(message = "Customer email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Customer phone is required")
    @Pattern(regexp = "^01[0-9]{9}$", message = "Phone must be 11 digits starting with 01 (e.g. 01012345678)")
    private String phone;

    @NotNull(message = "Customer enabled flag is required")
    private Boolean enabled;

    @PastOrPresent(message = "Creation date cannot be in the future")
    private Timestamp createdAt;

    @Valid
    private List<String> roles = new ArrayList<>();

    @Valid
    private List<String> authorities = new ArrayList<>();

}
