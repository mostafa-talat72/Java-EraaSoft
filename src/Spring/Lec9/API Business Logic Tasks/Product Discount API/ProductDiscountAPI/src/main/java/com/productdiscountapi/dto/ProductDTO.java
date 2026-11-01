package com.productdiscountapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

// Full product view — the nested category is the light SimpleDTO.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    private Long id;

    private String name;

    private BigDecimal price;

    private Boolean discountEligible;

    private CategorySimpleDTO category;
}
