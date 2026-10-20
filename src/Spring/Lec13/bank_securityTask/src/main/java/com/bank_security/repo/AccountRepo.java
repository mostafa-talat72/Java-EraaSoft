package com.bank_security.repo;

import com.bank_security.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepo extends JpaRepository<Account, Long> {

    Optional<Account> findByAccountNumber(String accountNumber);

    // Uniqueness guard for generated numbers (checked before save).
    boolean existsByAccountNumber(String accountNumber);
}
