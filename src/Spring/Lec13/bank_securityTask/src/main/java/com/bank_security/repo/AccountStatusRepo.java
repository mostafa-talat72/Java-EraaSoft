package com.bank_security.repo;

import com.bank_security.model.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountStatusRepo extends JpaRepository<AccountStatus, Long> {

    public Optional<AccountStatus> findByName(String name);

}
