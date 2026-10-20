package com.bank_security.repo;

import com.bank_security.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionRepo extends JpaRepository<Permission, Long> {

    public Optional<Permission> findByName(String name);

    public boolean existsByName(String name);

    public boolean existsByNameAndIdNot(String name, long id);
}
