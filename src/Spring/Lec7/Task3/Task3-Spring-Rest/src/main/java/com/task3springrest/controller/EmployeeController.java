package com.task3springrest.controller;

import com.task3springrest.model.Employee;
import com.task3springrest.service.EmployeeService;
import jakarta.transaction.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }


    // ==================== GET ====================

    // Get all employees
    // GET localhost:8085/employees
    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }


    // Get employees by a list of IDs
    // GET localhost:8085/employees/byIds?ids=1,2,3
    @GetMapping("/byIds")
    public List<Employee> getEmployeesByIds(@RequestParam List<Long> ids) {
        return employeeService.getEmployeesByIds(ids);
    }


    // ==================== POST ====================

    // Add one employee
    // POST localhost:8085/employees
    @PostMapping
    public Employee addEmployee(@RequestBody Employee employee) throws SystemException {
        return employeeService.addEmployee(employee);
    }


    // Add a list of employees
    // POST localhost:8085/employees/list
    @PostMapping("/list")
    public List<Employee> addListOfEmployees(
            @RequestBody List<Employee> employees) throws SystemException {

        return employeeService.addListOfEmployees(employees);
    }


    // ==================== PUT ====================

    // Update one employee
    // PUT localhost:8085/employees
    @PutMapping
    public Employee updateEmployee(@RequestBody Employee employee) throws SystemException {
        return employeeService.updateEmployee(employee);
    }


    // Update a list of employees
    // PUT localhost:8085/employees/list
    @PutMapping("/list")
    public List<Employee> updateListOfEmployees(
            @RequestBody List<Employee> employees) throws SystemException {

        return employeeService.updateListOfEmployees(employees);
    }


    // ==================== DELETE ====================

    // Delete all employees
    // DELETE localhost:8085/employees
    @DeleteMapping
    public void deleteAllEmployees() {
        employeeService.deleteAllEmployees();
    }


    // Delete one employee by ID
    // DELETE localhost:8085/employees/{id}
    @DeleteMapping("/{id}")
    public void deleteEmployeeById(@PathVariable long id) {
        employeeService.deleteEmployeeById(id);
    }


    // Delete a list of employees by their IDs
    // DELETE localhost:8085/employees/list
    @DeleteMapping("/list")
    public void deleteListOfEmployees(@RequestBody List<Long> ids) {
        employeeService.deleteListOfEmployees(ids);
    }


    // ==================== SEARCH ====================

    // Search employees by name
    //
    // This endpoint calls 3 different search methods:
    // 1. Derived Query
    // 2. Native Query
    // 3. JPQL Query
    //
    // Example:
    // GET localhost:8085/employees/search/name
    //
    // The result is returned as:
    // [
    //     [Derived Query results],
    //     [Native Query results],
    //     [JPQL Query results]
    // ]
    @GetMapping("/search/{name}")
    public List<List<Employee>> searchEmployeeByName(
            @PathVariable String name) {

        return Arrays.asList(
                employeeService.searchEmployeeByName(name),
                employeeService.searchByNameNative(name),
                employeeService.searchByNameJPQL(name)
        );
    }
}