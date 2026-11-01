package com.productdiscountapi.repo;

import com.productdiscountapi.model.DiscountCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DiscountCodeRepo extends JpaRepository<DiscountCode, Long> {

    // code is unique at the DB level -> lookup + duplicate checks in the service.
    Optional<DiscountCode> findByCode(String code);

    boolean existsByCode(String code);

    boolean existsByCodeAndIdNot(String code, Long id);
}
