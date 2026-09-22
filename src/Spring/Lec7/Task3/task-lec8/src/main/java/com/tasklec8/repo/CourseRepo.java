package com.tasklec8.repo;

import com.tasklec8.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepo extends JpaRepository<Course, Long> {

    public boolean existsByTitle(String title);
}
