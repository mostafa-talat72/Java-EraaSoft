package com.productdiscountapi.repo;

import com.productdiscountapi.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Long> {

    // name is unique at the DB level -> pre-checked in the service layer.
    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);
}
