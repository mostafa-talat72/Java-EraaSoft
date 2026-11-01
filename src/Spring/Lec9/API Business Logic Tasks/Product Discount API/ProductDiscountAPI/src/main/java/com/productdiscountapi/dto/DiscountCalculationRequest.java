package com.productdiscountapi.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

// Body of POST /discounts/calculate — the whole calculation flow input.
// customerId + items are mandatory; discountCodes is optional (rule 6 checks
// incompatibility only when at least two codes arrive).
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiscountCalculationRequest {

    @NotNull(message = "Customer id is required")
    private Long customerId;

    // @Valid cascades validation into every item, so productId and quantity
    // are checked here instead of reaching the service and failing with a 500.
    @Valid
    @NotEmpty(message = "At least one order item is required")
    private List<DiscountItemRequest> items;

    private List<String> discountCodes;
}
