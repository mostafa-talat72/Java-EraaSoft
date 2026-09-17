package com.task3springrest.repo;

import com.task3springrest.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee, Long> {

    // Check if there is another employee with the same phone number.
    // "AndIdNot" means: find the phone number but ignore the employee
    // that has the given ID.
    // This is useful when updating an employee.
    boolean existsByPhoneNumberAndIdNot(String phoneNumber, Long id);


    // Check if an employee already exists with this phone number.
    // Used when adding a new employee.
    boolean existsByPhoneNumber(String phoneNumber);


    // =========================================================
    // 1. Derived Query
    // =========================================================
    // Spring Data JPA creates the SQL query automatically
    // from the method name.
    //
    // findByNameLike -> WHERE name LIKE ?
    //
    // Example:
    // findByNameLike("Ahmed%")
    // -> WHERE name LIKE 'Ahmed%'
    List<Employee> findByNameLike(String name);


    // =========================================================
    // 2. Native Query
    // =========================================================
    // Native Query means we write the SQL query ourselves.
    //
    // This query uses:
    // - Database table name: employee
    // - Database column name: name
    //
    // nativeQuery = true tells Spring that this is normal SQL,
    // not JPQL.
    @Query(
            value = "SELECT * FROM employee WHERE name LIKE :name",
            nativeQuery = true
    )
    List<Employee> searchByNameNative(@Param("name") String name);


    // =========================================================
    // 3. JPQL / Non-Native Query
    // =========================================================
    // JPQL works with Java Entity and Java field names,
    // NOT database table and column names.
    //
    // Employee -> Entity class
    // e.name   -> Java field
    //
    // Notice that we use "Employee" instead of "employee".
    //
    // JPQL:
    // SELECT e
    // FROM Employee e
    // WHERE e.name LIKE :name
    @Query("""
        SELECT e
        FROM Employee e
        WHERE e.name LIKE :name
    """)
    List<Employee> searchByNameJPQL(@Param("name") String name);
}