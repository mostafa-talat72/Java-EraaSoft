package com.productdiscountapi.service.impl;

import com.productdiscountapi.dto.ProductDTO;
import com.productdiscountapi.dto.ProductRequestDTO;
import com.productdiscountapi.exception.BadRequestException;
import com.productdiscountapi.exception.NotFoundException;
import com.productdiscountapi.mapper.ProductMapper;
import com.productdiscountapi.model.Category;
import com.productdiscountapi.model.Product;
import com.productdiscountapi.repo.ProductRepo;
import com.productdiscountapi.service.CategoryService;
import com.productdiscountapi.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepo productRepo;
    private final ProductMapper productMapper;
    private final CategoryService categoryService;

    @Transactional(readOnly = true)
    @Override
    public List<ProductDTO> getAllProducts() {
        // Read-only query to fetch all products registered in the system.
        List<Product> products = productRepo.findAll();

        if (products.isEmpty()) {
            throw new NotFoundException("No Products found in the system");
        }

        return productMapper.convertFromProductListToProductDtoList(products);
    }

    @Transactional(readOnly = true)
    @Override
    public Product getProductEntityById(Long id) {
        // Raw entity lookup for internal service validation (e.g. calculation engine).
        return findProductOrThrow(id);
    }

    @Transactional(readOnly = true)
    @Override
    public ProductDTO getProductById(Long id) {
        // Fetch product entity by ID and map to response DTO.
        return productMapper.convertFromProductToProductDto(findProductOrThrow(id));
    }

    @Override
    public ProductDTO createProduct(ProductRequestDTO requestDTO) {
        // Ensure product name is unique to prevent duplicate entries.
        if (productRepo.existsByName(requestDTO.getName())) {
            throw new BadRequestException("Product with name '" + requestDTO.getName() + "' already exists");
        }

        // Resolve the associated Category entity by ID (throws 404 if category does not exist).
        Category category = categoryService.getCategoryEntityById(requestDTO.getCategoryId());

        // Map request DTO to entity (ignoring category mapping inside MapStruct).
        Product product = productMapper.convertFromProductRequestDtoToProduct(requestDTO);
        product.setCategory(category);

        return productMapper.convertFromProductToProductDto(productRepo.save(product));
    }

    @Override
    public ProductDTO updateProduct(Long id, ProductRequestDTO requestDTO) {
        // Fetch existing product or throw 404.
        Product product = findProductOrThrow(id);

        // Ensure updated product name does not conflict with another existing product.
        if (productRepo.existsByNameAndIdNot(requestDTO.getName(), id)) {
            throw new BadRequestException("Product with name '" + requestDTO.getName() + "' already exists");
        }

        // Resolve the updated Category entity by ID (throws 404 if category does not exist).
        Category category = categoryService.getCategoryEntityById(requestDTO.getCategoryId());

        // In-place update using MapStruct target mapping.
        productMapper.updateProductFromProductRequestDto(requestDTO, product);
        product.setCategory(category);

        return productMapper.convertFromProductToProductDto(productRepo.save(product));
    }

    @Override
    public void deleteProduct(Long id) {
        // Ensure product exists before issuing a delete statement.
        Product product = findProductOrThrow(id);
        productRepo.delete(product);
    }

    // Shared helper for safe entity retrieval with consistent 404 messaging.
    private Product findProductOrThrow(Long id) {
        return productRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Product with id " + id + " not found"));
    }
}
