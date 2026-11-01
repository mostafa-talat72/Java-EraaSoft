package com.productdiscountapi.controller;

import com.productdiscountapi.dto.CategoryDTO;
import com.productdiscountapi.dto.CategoryRequestDTO;
import com.productdiscountapi.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    // GET /categories — list every category with its own discount percentage.
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    // GET /categories/{id} — one category; 404 through the service when missing.
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    // POST /categories — create; 201 with the saved representation.
    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(
            @Valid @RequestBody CategoryRequestDTO requestDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(categoryService.createCategory(requestDTO));
    }

    // PUT /categories/{id} — update an existing category.
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequestDTO requestDTO) {
        return ResponseEntity.ok(categoryService.updateCategory(id, requestDTO));
    }

    // DELETE /categories/{id} — 204 with no body on success.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
