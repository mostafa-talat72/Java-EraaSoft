package com.productdiscountapi.service;

import com.productdiscountapi.dto.CategoryDTO;
import com.productdiscountapi.dto.CategoryRequestDTO;
import com.productdiscountapi.model.Category;

import java.util.List;

public interface CategoryService {

    List<CategoryDTO> getAllCategories();

    Category getCategoryEntityById(Long id);

    CategoryDTO getCategoryById(Long id);

    CategoryDTO createCategory(CategoryRequestDTO requestDTO);

    CategoryDTO updateCategory(Long id, CategoryRequestDTO requestDTO);

    void deleteCategory(Long id);
}
