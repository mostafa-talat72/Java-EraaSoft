package com.restauranttablereservationapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Payload for POST/PUT /customers.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRequestDTO {

    @NotBlank(message = "Customer name is required")
    @Size(max = 100, message = "Customer name cannot exceed 100 characters")
    private String name;

    @NotBlank(message = "Customer phone is required")
    @Size(max = 20, message = "Customer phone cannot exceed 20 characters")
    private String phone;

    @Email(message = "Customer email must be valid")
    @Size(max = 100, message = "Customer email cannot exceed 100 characters")
    private String email;
}
