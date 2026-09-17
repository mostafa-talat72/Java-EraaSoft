package com.task3springrest.service.impl;

import com.task3springrest.model.Employee;
import com.task3springrest.repo.EmployeeRepo;
import com.task3springrest.service.EmployeeService;
import jakarta.transaction.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepo employeeRepo;

    // Constructor Injection:
    // Spring automatically injects EmployeeRepo into the Service.
    @Autowired
    public EmployeeServiceImpl(EmployeeRepo employeeRepo) {
        this.employeeRepo = employeeRepo;
    }


    // =========================================================
    // GET ALL EMPLOYEES
    // =========================================================

    @Override
    public List<Employee> getAllEmployees() {

        // findAll() is provided by JpaRepository.
        // It returns all employees from the database.
        return employeeRepo.findAll();
    }


    // =========================================================
    // GET EMPLOYEES BY IDS
    // =========================================================

    @Override
    public List<Employee> getEmployeesByIds(List<Long> ids) {

        // findAllById() searches for employees
        // using the IDs provided in the list.
        //
        // Example:
        // ids = [1, 2, 5]
        //
        // It returns employees with these IDs.
        return employeeRepo.findAllById(ids);
    }


    // =========================================================
    // ADD ONE EMPLOYEE
    // =========================================================

    @Override
    public Employee addEmployee(Employee employee)
            throws SystemException {

        // When adding a new employee,
        // the ID must be null because the database
        // generates the ID automatically.
        if (Objects.nonNull(employee.getId())) {
            throw new SystemException("ID must be null");
        }


        // Check if the phone number already exists.
        //
        // The phone number is unique,
        // so another employee cannot use the same number.
        if (employeeRepo.existsByPhoneNumber(
                employee.getPhoneNumber())) {

            throw new SystemException(
                    "This phone number already exists: "
                            + employee.getPhoneNumber()
            );
        }


        // Save the employee into the database.
        return employeeRepo.save(employee);
    }


    // =========================================================
    // ADD LIST OF EMPLOYEES
    // =========================================================

    @Override
    public List<Employee> addListOfEmployees(
            List<Employee> employees) throws SystemException {

        // Find ALL employees that have an ID.
        List<String> idErrors = employees.stream()
                .filter(employee -> Objects.nonNull(employee.getId()))
                .map(employee ->
                        "Employee ID must be null: "
                                + employee.getId())
                .toList();


        // If there are any ID errors,
        // throw one exception containing all errors.
        if (!idErrors.isEmpty()) {
            throw new SystemException(
                    String.join(", ", idErrors)
            );
        }


        // Find ALL employees whose phone number
        // already exists in the database.
        List<String> phoneErrors = employees.stream()
                .filter(employee ->
                        employeeRepo.existsByPhoneNumber(
                                employee.getPhoneNumber()))
                .map(employee ->
                        "Phone number already exists: "
                                + employee.getPhoneNumber())
                .toList();


        // If duplicate phone numbers were found, throw one exception containing all errors.
        if (!phoneErrors.isEmpty()) {
            throw new SystemException(
                    String.join(", ", phoneErrors)
            );
        }


        // All validations passed.
        // Save all employees at once.
        return employeeRepo.saveAll(employees);
    }


    // =========================================================
    // UPDATE ONE EMPLOYEE
    // =========================================================

    @Override
    public Employee updateEmployee(Employee employee)
            throws SystemException {

        // When updating an employee, the ID must not be null.
        if (Objects.isNull(employee.getId())) {
            throw new SystemException("ID must not be null");
        }


        // Check that the employee exists in the database.
        if (!employeeRepo.existsById(employee.getId())) {
            throw new SystemException(
                    "Employee not found with ID: "
                            + employee.getId()
            );
        }


        // Check if the phone number belongs to another employee.
        if (employeeRepo.existsByPhoneNumberAndIdNot(
                employee.getPhoneNumber(),
                employee.getId())) {

            throw new SystemException(
                    "This phone number already exists: "
                            + employee.getPhoneNumber()
            );
        }


        // Save the updated employee.
        return employeeRepo.save(employee);
    }


    // =========================================================
    // UPDATE LIST OF EMPLOYEES
    // =========================================================

    @Override
    public List<Employee> updateListOfEmployees(
            List<Employee> employees) throws SystemException {

        // Find ALL employees that do not have an ID.
        //
        // Every employee must have an ID when updating.
        List<String> idErrors = employees.stream()
                .filter(employee -> Objects.isNull(employee.getId()))
                .map(employee ->
                        "Employee ID must not be null")
                .toList();


        // If any employee has a null ID,
        // return all ID validation errors.
        if (!idErrors.isEmpty()) {
            throw new SystemException(
                    String.join(", ", idErrors)
            );
        }


        // Find ALL employees whose IDs
        // do not exist in the database.
        List<String> notFoundErrors = employees.stream()
                .filter(employee ->
                        !employeeRepo.existsById(employee.getId()))
                .map(employee ->
                        "Employee not found with ID: "
                                + employee.getId())
                .toList();


        // If any employee was not found,
        // return all not-found errors.
        if (!notFoundErrors.isEmpty()) {
            throw new SystemException(
                    String.join(", ", notFoundErrors)
            );
        }


        // Find ALL employees whose phone number belongs to another employee.
        List<String> phoneErrors = employees.stream()
                .filter(employee ->
                        employeeRepo.existsByPhoneNumberAndIdNot(
                                employee.getPhoneNumber(),
                                employee.getId()))
                .map(employee ->
                        "Phone number already exists: "
                                + employee.getPhoneNumber()
                                + " for employee ID: "
                                + employee.getId())
                .toList();


        // If duplicate phone numbers were found, return all duplicate phone number errors.
        if (!phoneErrors.isEmpty()) {
            throw new SystemException(
                    String.join(", ", phoneErrors)
            );
        }


        // All validations passed.
        // Save all updated employees.
        return employeeRepo.saveAll(employees);
    }


    // =========================================================
    // DELETE ALL EMPLOYEES
    // =========================================================

    @Override
    public void deleteAllEmployees() {

        // Delete all employees from the database.
        employeeRepo.deleteAll();
    }


    // =========================================================
    // DELETE ONE EMPLOYEE
    // =========================================================

    @Override
    public void deleteEmployeeById(long id) {

        // Delete the employee using its ID.
        employeeRepo.deleteById(id);
    }


    // =========================================================
    // DELETE LIST OF EMPLOYEES
    // =========================================================

    @Override
    public void deleteListOfEmployees(List<Long> ids) {

        // Delete all employees whose IDs
        employeeRepo.deleteAllById(ids);
    }


    // =========================================================
    // SEARCH BY NAME - DERIVED QUERY
    // =========================================================

    @Override
    public List<Employee> searchEmployeeByName(String name) {

        // Add "%" after the name.
        //
        // Example:
        // name = "Ahmed"
        //
        // Becomes:
        // "Ahmed%"
        //
        // SQL:
        // WHERE name LIKE 'Ahmed%'
        //
        // This finds names that START with "Ahmed".
        return employeeRepo.findByNameLike(name + "%");
    }


    // =========================================================
    // SEARCH BY NAME - NATIVE QUERY
    // =========================================================

    @Override
    public List<Employee> searchByNameNative(String name) {

        // Pass "Ahmed%" to the Native SQL query.
        //
        // SQL becomes:
        // SELECT * FROM employee
        // WHERE name LIKE 'Ahmed%'
        return employeeRepo.searchByNameNative(name + "%");
    }


    // =========================================================
    // SEARCH BY NAME - JPQL
    // =========================================================

    @Override
    public List<Employee> searchByNameJPQL(String name) {

        // Pass "Ahmed%" to the JPQL query.
        //
        // JPQL becomes:
        // SELECT e
        // FROM Employee e
        // WHERE e.name LIKE 'Ahmed%'
        return employeeRepo.searchByNameJPQL(name + "%");
    }
}