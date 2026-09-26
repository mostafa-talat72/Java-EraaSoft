package com.lec9.employeeemailtask.repo;

import com.lec9.employeeemailtask.model.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmailRepo extends JpaRepository<Email, Long> {

    // Unique-content guards used by EmailValidator/service.
    boolean existsByContent(String content);

    // Update-safe unique check: content used by another id?
    boolean existsByContentAndIdNot(String content, long id);

    // Exact-content lookup; case-insensitive name lookups.
    Email findByContent(String content);

    List<Email> findByNameIgnoreCase(String name);

    List<Email> findAllByNameInIgnoreCase(List<String> names);
}
