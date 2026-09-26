package com.lec9.employeeemailtask.service.impl;

import com.lec9.employeeemailtask.dto.EmployeeResponse;
import com.lec9.employeeemailtask.exception.EmployeeException;
import com.lec9.employeeemailtask.mapper.EmailMapper;
import com.lec9.employeeemailtask.mapper.EmployeeMapper;
import com.lec9.employeeemailtask.model.Email;
import com.lec9.employeeemailtask.model.Employee;
import com.lec9.employeeemailtask.repo.EmployeeRepo;
import com.lec9.employeeemailtask.service.EmployeeService;
import com.lec9.employeeemailtask.validation.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepo employeeRepo;
    private EmployeeMapper employeeMapper;
    private EmailMapper emailMapper;
    private EmailValidator emailValidator;
    @Autowired
    public EmployeeServiceImpl(EmployeeRepo employeeRepo, EmployeeMapper employeeMapper, EmailMapper emailMapper,  EmailValidator emailValidator) {
        this.employeeRepo = employeeRepo;
        this.employeeMapper = employeeMapper;
        this.emailMapper = emailMapper;
        this.emailValidator = emailValidator;
    }

    /**
     * Create a new Employee.
     *
     * The ID must be null because it will be generated automatically
     * by the database.
     */
    @Override
    public EmployeeResponse createEmployee(EmployeeResponse employeeResponse) {

        // Check that the client did not send an ID when creating a new Employee
        if (Objects.nonNull(employeeResponse.getId())) {
            throw new EmployeeException(
                    "id",
                    "Employee ID must be null when creating a new employee"
            );
        }

        // Convert DTO to Entity
        Employee employee =
                employeeMapper.convertFromEmployeeResponseToEmployee(employeeResponse);

        if(Objects.nonNull(employeeResponse.getEmails())) {
            emailValidator.checkContentForCreate(employeeResponse.getEmails());

            List<Email> emails =
                    emailMapper.convertFromEmailSimpleResponseListToEmailList(
                            employeeResponse.getEmails()
                    );
            emails.forEach(email -> {
                email.setEmployee(employee);
                employee.getEmails().add(email);
            });
        }
        // Save the Employee and convert the saved Entity back to DTO
        return employeeMapper.convertFromEmployeeToEmployeeResponse(
                employeeRepo.save(employee)
        );
    }

    /**
     * Update an existing Employee.
     *
     * The ID must be provided and the Employee must already exist.
     */
    @Override
    public EmployeeResponse updateEmployee(EmployeeResponse employeeResponse) {

        // ID is required when updating an Employee
        if (Objects.isNull(employeeResponse.getId())) {
            throw new EmployeeException(
                    "id",
                    "Employee ID must not be null when updating an employee"
            );
        }

        // Check that the Employee exists before updating
        if (!employeeRepo.existsById(employeeResponse.getId())) {
            throw new EmployeeException(
                    "id",
                    "Employee does not exist with ID: " + employeeResponse.getId()
            );
        }

        // Convert DTO to Entity
        Employee employee =
                employeeMapper.convertFromEmployeeResponseToEmployee(employeeResponse);

        if(Objects.nonNull(employeeResponse.getEmails())) {
            emailValidator.checkContentForUpdate(employeeResponse.getEmails());

            List<Email> emails =
                    emailMapper.convertFromEmailSimpleResponseListToEmailList(
                            employeeResponse.getEmails()
                    );
            emails.forEach(email -> {
                email.setEmployee(employee);
                employee.getEmails().add(email);
            });
        }
        // Save the updated Employee and return the updated DTO
        return employeeMapper.convertFromEmployeeToEmployeeResponse(
                employeeRepo.save(employee)
        );
    }

    /**
     * Delete an Employee by ID.
     *
     * The Employee must exist before deleting it.
     */
    @Override
    public void deleteEmployee(long id) {

        // Check that the Employee exists
        if (!employeeRepo.existsById(id)) {
            throw new EmployeeException(
                    "id",
                    "Employee does not exist with ID: " + id
            );
        }

        // Delete the Employee
        employeeRepo.deleteById(id);
    }

    /**
     * Get all Employees.
     *
     * Throws an exception if no Employees exist.
     */
    @Override
    public List<EmployeeResponse> getAllEmployees() {

        // Retrieve all Employees from the database
        List<Employee> employees = employeeRepo.findAll();

        // Check if the database contains any Employees
        if (employees.isEmpty()) {
            throw new EmployeeException(
                    "Employees",
                    "No employees found"
            );
        }

        // Convert Entity list to DTO list
        return employeeMapper.convertFromEmployeeListToEmployeeResponseList(employees);
    }

    /**
     * Get an Employee by ID.
     *
     * Uses Optional.orElseThrow() to handle the case
     * where the Employee does not exist.
     */
    @Override
    public EmployeeResponse getEmployeeById(long id) {

        // Find the Employee by ID
        Employee employee = employeeRepo.findById(id)
                .orElseThrow(() -> new EmployeeException(
                        "id",
                        "Employee does not exist with ID: " + id
                ));

        // Convert Entity to DTO and return it
        return employeeMapper.convertFromEmployeeToEmployeeResponse(employee);
    }

    /**
     * Get Employees by a list of IDs.
     *
     * The method also checks whether all requested IDs exist.
     */
    @Override
    public List<EmployeeResponse> getEmployeesByListOfIds(List<Long> ids) {

        // Retrieve all Employees that match the provided IDs
        List<Employee> employees = employeeRepo.findAllById(ids);

        // Extract the IDs of Employees that were actually found
        Set<Long> foundIds = employees.stream()
                .map(employee -> employee.getId())
                .collect(Collectors.toSet());

        // Find the requested IDs that do not exist in the database
        List<Long> missingIds = ids.stream()
                .distinct()
                .filter(id -> !foundIds.contains(id))
                .toList();

        // If any ID is missing, throw an exception
        if (!missingIds.isEmpty()) {
            throw new EmployeeException(
                    "Employees",
                    "Employees with the following IDs were not found: " + missingIds
            );
        }

        // Convert Entity list to DTO list
        return employeeMapper.convertFromEmployeeListToEmployeeResponseList(employees);
    }

    /**
     * Get Employees by a list of names.
     *
     * The method also checks whether all requested names exist.
     */
    @Override
    public List<EmployeeResponse> getEmployeesByListOfNames(List<String> names) {

        // Retrieve all Employees whose names match the provided names
        List<Employee> employees = employeeRepo.findAllByNameInIgnoreCase(names);

        // Extract the names of Employees that were actually found
        Set<String> foundNames = employees.stream()
                .map(employee -> employee.getName())
                .collect(Collectors.toSet());

        // Find the requested names that do not exist in the database
        List<String> missingNames = names.stream()
                .distinct()
                .filter(name -> !foundNames.contains(name))
                .toList();

        // If any name is missing, throw an exception
        if (!missingNames.isEmpty()) {
            throw new EmployeeException(
                    "Employees",
                    "Employees with the following names were not found: " + missingNames
            );
        }

        // Convert Entity list to DTO list
        return employeeMapper.convertFromEmployeeListToEmployeeResponseList(employees);
    }
}