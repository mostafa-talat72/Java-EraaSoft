package com.productdiscountapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiscountCodeDTO {

    private Long id;

    private String code;

    private Integer discountPercentage;

    // The codes this code cannot be combined with (rule 6).
    private List<DiscountCodeSimpleDTO> incompatibleCodes;
}
