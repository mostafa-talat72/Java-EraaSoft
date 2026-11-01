package com.productdiscountapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

// Light product reference — used in the calculation breakdown items.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductSimpleDTO {

    private Long id;

    private String name;

    private BigDecimal price;
}
