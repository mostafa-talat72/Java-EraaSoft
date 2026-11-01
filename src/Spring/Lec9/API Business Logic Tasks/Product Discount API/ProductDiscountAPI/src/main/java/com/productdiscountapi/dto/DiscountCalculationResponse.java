package com.productdiscountapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

// Result of POST /discounts/calculate — every number the client needs:
// subtotal (before discounts), totalDiscount (sum of all applied ones),
// shippingCost (0 when the order qualifies for free shipping) and finalPrice
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiscountCalculationResponse {

    private BigDecimal subtotal;

    private BigDecimal totalDiscount;

    private BigDecimal shippingCost;

    private BigDecimal finalPrice;
}
