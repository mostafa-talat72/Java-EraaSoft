package com.bank_security.repo;

import com.bank_security.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepo extends JpaRepository<Role, Long> {

    public Optional<Role> findByName(String name);

    public boolean existsByName(String name);

    public boolean existsByNameAndIdNot(String name, long id);
}
