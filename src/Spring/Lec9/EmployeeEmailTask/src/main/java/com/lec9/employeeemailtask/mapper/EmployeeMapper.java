package com.lec9.employeeemailtask.mapper;

import com.lec9.employeeemailtask.dto.EmployeeResponse;
import com.lec9.employeeemailtask.dto.EmployeeSimpleResponse;
import com.lec9.employeeemailtask.model.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    @Mapping(target = "emails", ignore = true)
    Employee convertFromEmployeeResponseToEmployee(EmployeeResponse employeeResponse);

    EmployeeResponse convertFromEmployeeToEmployeeResponse(Employee employee);

    List<Employee> convertFromEmployeeResponseListToEmployeeList(List<EmployeeResponse> employeeResponseList);

    List<EmployeeResponse> convertFromEmployeeListToEmployeeResponseList(List<Employee> employees);

    Employee convertFromEmployeeSimpleResponseToEmployee(EmployeeSimpleResponse employeeSimpleResponse);

    EmployeeSimpleResponse convertFromEmployeeToEmployeeSimpleResponse(Employee employee);

    List<Employee> convertFromEmployeeSimpleResponseListToEmployeeList(List<EmployeeSimpleResponse> employeeSimpleResponseList);

    List<EmployeeSimpleResponse> convertFromEmployeeListToEmployeeSimpleResponseList(List<Employee> employees);

}
