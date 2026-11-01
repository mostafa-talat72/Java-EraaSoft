package com.productdiscountapi.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

// Payload for POST /products and PUT /products/{id}.
// The category is resolved by categoryId in the service layer.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDTO {

    @NotBlank(message = "Product name is required")
    @Size(max = 100, message = "Product name cannot exceed 100 characters")
    private String name;

    @NotNull(message = "Product price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Product price must be greater than zero")
    private BigDecimal price;

    @NotNull(message = "Discount eligibility flag is required")
    private Boolean discountEligible;

    @NotNull(message = "Category id is required")
    private Long categoryId;
}
