package com.productdiscountapi.repo;

import com.productdiscountapi.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {

    // name is unique at the DB level -> pre-checked in the service layer.
    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);
}
