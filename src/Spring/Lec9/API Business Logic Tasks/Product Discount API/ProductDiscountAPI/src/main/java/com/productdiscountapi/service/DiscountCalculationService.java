package com.productdiscountapi.service;

import com.productdiscountapi.dto.DiscountCalculationRequest;
import com.productdiscountapi.dto.DiscountCalculationResponse;

public interface DiscountCalculationService {

    DiscountCalculationResponse calculateDiscount(DiscountCalculationRequest request);

}
