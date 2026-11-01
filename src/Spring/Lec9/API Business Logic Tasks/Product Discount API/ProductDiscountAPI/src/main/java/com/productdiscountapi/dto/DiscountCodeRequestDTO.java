package com.productdiscountapi.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

// Payload for POST /discount-codes and PUT /discount-codes/{id}.
// Incompatible codes are declared by ids and resolved in the service layer.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiscountCodeRequestDTO {

    @NotBlank(message = "Discount code is required")
    @Size(max = 50, message = "Discount code cannot exceed 50 characters")
    private String code;

    @NotNull(message = "Discount percentage is required")
    @Min(value = 0, message = "Discount percentage cannot be negative")
    @Max(value = 100, message = "Discount percentage cannot exceed 100")
    private Integer discountPercentage;

    // Ids of the codes this one conflicts with (rule 6); optional.
    private List<Long> incompatibleCodeIds;
}
