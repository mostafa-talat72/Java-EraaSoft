package com.productdiscountapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotBlank(message = "Category name is required")
    @Column(nullable = false, unique = true)
    private String name;

    // Business rule 2: every category can carry its own discount percentage.
    // NOT unique: two categories may perfectly share the same percentage.
    @NotNull(message = "Discount percentage is required")
    @Min(value = 0, message = "Discount percentage cannot be negative")
    @Max(value = 100, message = "Discount percentage cannot exceed 100")
    @Column(nullable = false, unique = false)
    private Integer discountPercentage;

    @OneToMany(mappedBy = "category")
    private List<Product> products;
}
