package com.productdiscountapi.mapper;

import com.productdiscountapi.dto.ProductDTO;
import com.productdiscountapi.dto.ProductRequestDTO;
import com.productdiscountapi.dto.ProductSimpleDTO;
import com.productdiscountapi.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = CategoryMapper.class)
public interface ProductMapper {

    ProductDTO convertFromProductToProductDto(Product product);

    ProductSimpleDTO convertFromProductToProductSimpleDto(Product product);

    Product convertFromProductRequestDtoToProduct(ProductRequestDTO requestDTO);

    List<ProductDTO> convertFromProductListToProductDtoList(List<Product> products);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    void updateProductFromProductRequestDto(ProductRequestDTO requestDTO, @MappingTarget Product product);

}
