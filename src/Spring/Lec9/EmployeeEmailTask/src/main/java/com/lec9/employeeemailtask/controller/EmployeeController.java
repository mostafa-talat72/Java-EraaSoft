package com.lec9.employeeemailtask.controller;

import com.lec9.employeeemailtask.dto.EmployeeResponse;
import com.lec9.employeeemailtask.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /*
     * Create a new Employee
     *
     * HTTP Method: POST
     * URL: /employees
     *
     * @Valid:
     * Validates the request body using Bean Validation annotations.
     *
     * @RequestBody:
     * Converts the JSON request body into EmployeeResponse object.
     *
     * Response:
     * 201 Created
     * Returns the created employee.
     */
    @PostMapping
    public ResponseEntity<EmployeeResponse> createEmployee(
            @Valid @RequestBody EmployeeResponse employeeResponse) {

        EmployeeResponse response =
                employeeService.createEmployee(employeeResponse);

        // Location header contains the URL of the newly created employee
        return ResponseEntity
                .created(URI.create("/employees/" + response.getId()))
                .body(response);
    }


    /*
     * Update an existing Employee
     *
     * HTTP Method: PUT
     * URL: /employees
     *
     * The Employee ID should be included in the request body.
     *
     * @Valid:
     * Validates the employee data before updating.
     */
    @PutMapping
    public ResponseEntity<EmployeeResponse> updateEmployee(
            @Valid @RequestBody EmployeeResponse employeeResponse) {

        EmployeeResponse response =
                employeeService.updateEmployee(employeeResponse);

        return ResponseEntity.ok(response);
    }


    /*
     * Delete an Employee by ID
     *
     * HTTP Method: DELETE
     * URL: /employees/{id}
     *
     * @PathVariable:
     * Gets the employee ID from the URL.
     *
     * Response:
     * 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(
            @PathVariable long id) {

        employeeService.deleteEmployee(id);

        return ResponseEntity.noContent().build();
    }


    /*
     * Get all Employees
     *
     * HTTP Method: GET
     * URL: /employees
     *
     * Returns a list containing all employees.
     */
    @GetMapping
    public ResponseEntity<List<EmployeeResponse>> getAllEmployees() {

        List<EmployeeResponse> employeeResponseList =
                employeeService.getAllEmployees();

        return ResponseEntity.ok(employeeResponseList);
    }


    /*
     * Get one Employee by ID
     *
     * HTTP Method: GET
     * URL: /employees/{id}
     *
     * @PathVariable:
     * Gets the employee ID from the URL.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> getEmployeeById(
            @PathVariable long id) {

        EmployeeResponse response =
                employeeService.getEmployeeById(id);

        return ResponseEntity.ok(response);
    }


    /*
     * Get Employees by a list of IDs
     *
     * HTTP Method: GET
     * URL: /employees/by-ids?ids=1,2,5
     *
     * @RequestParam:
     * Reads the IDs from the query parameters.
     */
    @GetMapping("/by-ids")
    public ResponseEntity<List<EmployeeResponse>> getEmployeesByListOfIds(
            @RequestParam List<Long> ids) {

        List<EmployeeResponse> employeeResponseList =
                employeeService.getEmployeesByListOfIds(ids);

        return ResponseEntity.ok(employeeResponseList);
    }


    /*
     * Get Employees by a list of names
     *
     * HTTP Method: GET
     * URL: /employees/by-names?names=Ahmed,Mostafa
     *
     * @RequestParam:
     * Reads the names from the query parameters.
     */
    @GetMapping("/by-names")
    public ResponseEntity<List<EmployeeResponse>> getEmployeesByListOfNames(
            @RequestParam List<String> names) {

        List<EmployeeResponse> employeeResponseList =
                employeeService.getEmployeesByListOfNames(names);

        return ResponseEntity.ok(employeeResponseList);
    }
}