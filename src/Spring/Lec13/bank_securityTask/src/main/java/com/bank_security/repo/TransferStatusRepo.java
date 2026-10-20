package com.bank_security.repo;

import com.bank_security.model.TransferStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransferStatusRepo extends JpaRepository<TransferStatus, Long> {

    public Optional<TransferStatus> findByName(String name);
}
