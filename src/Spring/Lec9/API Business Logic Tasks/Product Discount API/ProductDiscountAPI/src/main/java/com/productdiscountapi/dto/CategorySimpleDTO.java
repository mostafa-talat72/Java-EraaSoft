package com.productdiscountapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Nested inside ProductDTO — keeps the response light (no products list).
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategorySimpleDTO {

    private Long id;

    private String name;
}
