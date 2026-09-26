package com.lec9.employeeemailtask.repo;

import com.lec9.employeeemailtask.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee, Long> {

    // Case-insensitive bulk lookup by names.
    List<Employee> findAllByNameInIgnoreCase(List<String> names);
}
