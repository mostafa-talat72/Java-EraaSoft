package com.productdiscountapi.mapper;

import com.productdiscountapi.dto.CategoryDTO;
import com.productdiscountapi.dto.CategoryRequestDTO;
import com.productdiscountapi.dto.CategorySimpleDTO;
import com.productdiscountapi.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryDTO convertFromCategoryToCategoryDto(Category category);

    CategorySimpleDTO convertFromCategoryToCategorySimpleDto(Category category);

    Category convertFromCategoryRequestDtoToCategory(CategoryRequestDTO requestDTO);

    List<CategoryDTO> convertFromCategoryListToCategoryDtoList(List<Category> categories);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "products", ignore = true)
    void updateCategoryFromCategoryRequestDto(CategoryRequestDTO requestDTO, @MappingTarget Category category);
}
