package com.tasklec8.repo;

import com.tasklec8.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepo extends JpaRepository<Student, Long> {

    public boolean existsByEmail(String email);
}
