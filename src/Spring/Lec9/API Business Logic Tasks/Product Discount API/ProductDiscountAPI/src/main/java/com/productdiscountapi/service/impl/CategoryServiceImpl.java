package com.productdiscountapi.service.impl;

import com.productdiscountapi.dto.CategoryDTO;
import com.productdiscountapi.dto.CategoryRequestDTO;
import com.productdiscountapi.exception.BadRequestException;
import com.productdiscountapi.exception.NotFoundException;
import com.productdiscountapi.mapper.CategoryMapper;
import com.productdiscountapi.model.Category;
import com.productdiscountapi.repo.CategoryRepo;
import com.productdiscountapi.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepo categoryRepo;
    private final CategoryMapper categoryMapper;

    @Transactional(readOnly = true)
    @Override
    public List<CategoryDTO> getAllCategories() {
        // Read-only query to fetch all categories in the system.
        List<Category> categories = categoryRepo.findAll();

        if (categories.isEmpty()) {
            throw new NotFoundException("No categories found in the system");
        }

        return categoryMapper.convertFromCategoryListToCategoryDtoList(categories);
    }

    @Transactional(readOnly = true)
    @Override
    public Category getCategoryEntityById(Long id) {
        // Raw entity lookup — used internally by other services (e.g. ProductServiceImpl).
        return findCategoryOrThrow(id);
    }

    @Transactional(readOnly = true)
    @Override
    public CategoryDTO getCategoryById(Long id) {
        // Fetch category entity and map it to response DTO (404 if missing).
        return categoryMapper.convertFromCategoryToCategoryDto(findCategoryOrThrow(id));
    }

    @Override
    public CategoryDTO createCategory(CategoryRequestDTO requestDTO) {
        // Enforce unique category name at the service level to prevent raw DB constraint 500 errors.
        if (categoryRepo.existsByName(requestDTO.getName())) {
            throw new BadRequestException("Category with name '" + requestDTO.getName() + "' already exists");
        }

        // The 30% total-discount cap is enforced in the calculation flow only,
        // so a category keeps exactly the percentage it was created with.
        Category category = categoryMapper.convertFromCategoryRequestDtoToCategory(requestDTO);
        return categoryMapper.convertFromCategoryToCategoryDto(categoryRepo.save(category));
    }

    @Override
    public CategoryDTO updateCategory(Long id, CategoryRequestDTO requestDTO) {
        // Fetch existing category or throw 404.
        Category category = findCategoryOrThrow(id);

        // Ensure updated name does not conflict with another existing category.
        if (categoryRepo.existsByNameAndIdNot(requestDTO.getName(), category.getId())) {
            throw new BadRequestException("Category with name '" + requestDTO.getName() + "' already exists");
        }

        // In-place update preserving entity persistence state and relationships.
        categoryMapper.updateCategoryFromCategoryRequestDto(requestDTO, category);
        return categoryMapper.convertFromCategoryToCategoryDto(categoryRepo.save(category));
    }

    @Override
    public void deleteCategory(Long id) {
        // Ensure category exists before issuing a delete statement.
        Category category = findCategoryOrThrow(id);
        categoryRepo.delete(category);
    }

    // Shared helper for safe entity retrieval with consistent 404 messaging.
    private Category findCategoryOrThrow(Long id) {
        return categoryRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Category with id " + id + " not found"));
    }
}
