package com.bank_security.repo;

import com.bank_security.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, Long> {
    public Optional<Customer> findByEmail(String email);

    public boolean existsByEmail(String email);

    public boolean existsByEmailAndIdNot(String email, long id);

    public long countByEnabledTrue();

}
