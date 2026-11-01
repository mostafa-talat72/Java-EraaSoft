package com.productdiscountapi.controller;

import com.productdiscountapi.dto.DiscountCalculationRequest;
import com.productdiscountapi.dto.DiscountCalculationResponse;
import com.productdiscountapi.service.DiscountCalculationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/discounts")
@RequiredArgsConstructor
public class DiscountCalculationController {

    private final DiscountCalculationService discountCalculationService;

    // POST /discounts/calculate — the order goes in, priced totals come out.
    // @Valid also cascades into every item line inside the request (see the DTO).
    @PostMapping("/calculate")
    public ResponseEntity<DiscountCalculationResponse> calculateDiscount(
            @Valid @RequestBody DiscountCalculationRequest request) {
        return ResponseEntity.ok(discountCalculationService.calculateDiscount(request));
    }
}
