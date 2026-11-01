package com.productdiscountapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Light code reference for nesting inside DiscountCodeDTO.incompatibleCodes.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiscountCodeSimpleDTO {

    private Long id;

    private String code;

    private Integer discountPercentage;
}
