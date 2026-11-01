package com.productdiscountapi.service;

import com.productdiscountapi.dto.ProductDTO;
import com.productdiscountapi.dto.ProductRequestDTO;
import com.productdiscountapi.model.Product;

import java.util.List;

public interface ProductService {

    List<ProductDTO> getAllProducts();

    Product getProductEntityById(Long id);

    ProductDTO getProductById(Long id);

    ProductDTO createProduct(ProductRequestDTO requestDTO);

    ProductDTO updateProduct(Long id, ProductRequestDTO requestDTO);

    void deleteProduct(Long id);
}
