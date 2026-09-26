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
// Employee REST API: CRUD + bulk lookups. All writes validated via @Valid.
public class EmployeeController {

    private EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // POST /employees -> 201 Created + Location header.
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


    // PUT /employees (id in body) -> 200 OK.
    @PutMapping
    public ResponseEntity<EmployeeResponse> updateEmployee(
            @Valid @RequestBody EmployeeResponse employeeResponse) {

        EmployeeResponse response =
                employeeService.updateEmployee(employeeResponse);

        return ResponseEntity.ok(response);
    }


    // DELETE /employees/{id} -> 204 No Content.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(
            @PathVariable long id) {

        employeeService.deleteEmployee(id);

        return ResponseEntity.noContent().build();
    }


    // GET /employees -> 200 OK list.
    @GetMapping
    public ResponseEntity<List<EmployeeResponse>> getAllEmployees() {

        List<EmployeeResponse> employeeResponseList =
                employeeService.getAllEmployees();

        return ResponseEntity.ok(employeeResponseList);
    }


    // GET /employees/{id} -> 200 OK single.
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> getEmployeeById(
            @PathVariable long id) {

        EmployeeResponse response =
                employeeService.getEmployeeById(id);

        return ResponseEntity.ok(response);
    }


    // GET /employees/by-ids?ids=1,2,5 -> 200 OK.
    @GetMapping("/by-ids")
    public ResponseEntity<List<EmployeeResponse>> getEmployeesByListOfIds(
            @RequestParam List<Long> ids) {

        List<EmployeeResponse> employeeResponseList =
                employeeService.getEmployeesByListOfIds(ids);

        return ResponseEntity.ok(employeeResponseList);
    }


    // GET /employees/by-names?names=Ahmed,Mostafa -> 200 OK.
    @GetMapping("/by-names")
    public ResponseEntity<List<EmployeeResponse>> getEmployeesByListOfNames(
            @RequestParam List<String> names) {

        List<EmployeeResponse> employeeResponseList =
                employeeService.getEmployeesByListOfNames(names);

        return ResponseEntity.ok(employeeResponseList);
    }
}