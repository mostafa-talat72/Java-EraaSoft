package com.banktransferapi.repo;

import com.banktransferapi.model.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountTypeRepo extends JpaRepository<AccountType, Long> {

    Optional<AccountType> findByName(String name);

    // name is unique at the DB level -> pre-checked in the service layer.
    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);
}
