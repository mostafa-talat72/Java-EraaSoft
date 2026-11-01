package com.productdiscountapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Payload for POST /customers and PUT /customers/{id}.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRequestDTO {

    @NotBlank(message = "Customer name is required")
    @Size(max = 100, message = "Customer name cannot exceed 100 characters")
    private String name;

    @NotNull(message = "VIP flag is required")
    private Boolean vip;
}
