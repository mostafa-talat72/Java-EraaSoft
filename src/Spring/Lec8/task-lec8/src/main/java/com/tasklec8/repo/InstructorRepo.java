package com.tasklec8.repo;

import com.tasklec8.model.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.nio.file.LinkOption;

@Repository
public interface InstructorRepo extends JpaRepository<Instructor, Long> {

    public boolean existsByEmail(String email);
}
