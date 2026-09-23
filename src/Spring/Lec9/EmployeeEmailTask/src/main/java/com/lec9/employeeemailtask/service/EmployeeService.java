package com.lec9.employeeemailtask.service;

import com.lec9.employeeemailtask.dto.EmployeeResponse;

import java.util.List;

public interface EmployeeService {

    public EmployeeResponse createEmployee(EmployeeResponse employeeResponse);

    public EmployeeResponse updateEmployee(EmployeeResponse employeeResponse);

    public void deleteEmployee(long id);

    public List<EmployeeResponse> getAllEmployees();

    public EmployeeResponse getEmployeeById(long id);

    public List<EmployeeResponse> getEmployeesByListOfIds(List<Long> ids);

    public List<EmployeeResponse> getEmployeesByListOfNames(List<String> names);

}
