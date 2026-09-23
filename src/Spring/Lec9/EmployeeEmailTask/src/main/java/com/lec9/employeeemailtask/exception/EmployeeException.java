package com.lec9.employeeemailtask.exception;

import lombok.Getter;

@Getter
public class EmployeeException extends RuntimeException{
    private String field;

    public EmployeeException(String field, String message) {
        super(message);
        this.field = field;
    }
}
