package com.bank_security.repo;

import com.bank_security.model.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransferRepo extends JpaRepository<Transfer, Long> {
}
