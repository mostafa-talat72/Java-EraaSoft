package com.task3springrest.service;

import com.task3springrest.model.Employee;
import jakarta.transaction.SystemException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmployeeService {

    public List<Employee> getAllEmployees();

    public  List<Employee> getEmployeesByIds(List<Long> ids);

    public Employee addEmployee( Employee employee) throws SystemException;

    public List<Employee> addListOfEmployees(List<Employee> employees) throws SystemException;

    public Employee updateEmployee( Employee employee) throws SystemException;

    public List<Employee> updateListOfEmployees(List<Employee> employees) throws SystemException;

    public void deleteAllEmployees();

    public void deleteEmployeeById(long id);

    public void deleteListOfEmployees(List<Long> ids);

    public List<Employee> searchEmployeeByName(String name);

    List<Employee> searchByNameNative(String name);

    List<Employee>  searchByNameJPQL(String name);
}
