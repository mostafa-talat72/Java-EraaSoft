package com.productdiscountapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotBlank(message = "Product name is required")
    @Column(nullable = false, unique = true)
    private String name;

    @NotNull(message = "Product price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Product price must be greater than zero")
    @Column(nullable = false, unique = false)
    private BigDecimal price;

    // Business rule 5: some products are not eligible for any discount.
    @NotNull(message = "Discount eligibility flag is required")
    @Column(nullable = false, unique = false)
    private Boolean discountEligible;

    @NotNull(message = "Category is required")
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
}
