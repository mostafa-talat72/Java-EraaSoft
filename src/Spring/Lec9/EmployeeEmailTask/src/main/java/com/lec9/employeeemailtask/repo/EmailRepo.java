package com.lec9.employeeemailtask.repo;

import com.lec9.employeeemailtask.model.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmailRepo extends JpaRepository<Email, Long> {

    boolean existsByContent(String content);

    boolean existsByContentAndIdNot(String content, long id);

    Email findByContent(String content);

    List<Email> findByName(String name);

    List<Email> findAllByNameIn(List<String> names);
}
