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
public class DiscountCode {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotBlank(message = "Discount code is required")
    @Column(nullable = false, unique = true)
    private String code;

    @NotNull(message = "Discount percentage is required")
    @Min(value = 0, message = "Discount percentage cannot be negative")
    @Max(value = 100, message = "Discount percentage cannot exceed 100")
    @Column(nullable = false)
    private Integer discountPercentage;

    // Business rule 6: the customer cannot combine two incompatible codes.
    // Self-referencing many-to-many: a code declares the codes it conflicts with.
    @ManyToMany
    @JoinTable(
            name = "incompatible_discount_codes",
            joinColumns = @JoinColumn(name = "discount_code_id"),
            inverseJoinColumns = @JoinColumn(name = "incompatible_code_id"),
            uniqueConstraints = @UniqueConstraint(
                    columnNames = {"discount_code_id", "incompatible_code_id"}
            )
    )
    private List<DiscountCode> incompatibleCodes;
}
