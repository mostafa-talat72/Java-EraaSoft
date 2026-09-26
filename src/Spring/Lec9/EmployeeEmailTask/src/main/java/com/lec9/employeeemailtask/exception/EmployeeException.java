package com.lec9.employeeemailtask.exception;

import lombok.Getter;

@Getter
// Business error for employee flows; handled as 400 with field detail.
public class EmployeeException extends RuntimeException{
    // Field name used as key in the errors map.
    private String field;

    public EmployeeException(String field, String message) {
        super(message);
        this.field = field;
    }
}
