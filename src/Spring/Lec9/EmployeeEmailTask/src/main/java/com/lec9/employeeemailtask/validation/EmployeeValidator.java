package com.lec9.employeeemailtask.validation;

import com.lec9.employeeemailtask.dto.EmployeeSimpleResponse;
import com.lec9.employeeemailtask.exception.EmailException;
import com.lec9.employeeemailtask.exception.EmployeeException;
import com.lec9.employeeemailtask.mapper.EmployeeMapper;
import com.lec9.employeeemailtask.model.Employee;
import com.lec9.employeeemailtask.repo.EmployeeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
public class EmployeeValidator {

    private EmployeeRepo employeeRepo;
    private EmployeeMapper employeeMapper;

    @Autowired
    public EmployeeValidator(EmployeeRepo employeeRepo,EmployeeMapper employeeMapper ) {
        this.employeeRepo = employeeRepo;
        this.employeeMapper = employeeMapper;
    }


    public EmployeeSimpleResponse checkEmployee(EmployeeSimpleResponse employeeSimpleResponse){
        if (employeeSimpleResponse == null) {
            throw new EmployeeException(
                    "employee",
                    "Employee object is required"
            );
        }

        if (employeeSimpleResponse.getId() == null) {
            throw new EmployeeException(
                    "employee",
                    "Employee id is required"
            );
        }
        Optional<Employee> employee = employeeRepo.findById(employeeSimpleResponse.getId());
        if(employee.isEmpty()) {
            throw new EmployeeException(
                    "id",
                    "Employee does not exist with ID: " + employeeSimpleResponse.getId()
            );
        }

        return employeeMapper.convertFromEmployeeToEmployeeSimpleResponse(employee.get());

    }

}
