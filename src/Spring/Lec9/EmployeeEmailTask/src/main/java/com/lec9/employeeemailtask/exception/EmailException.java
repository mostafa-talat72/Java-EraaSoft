package com.lec9.employeeemailtask.exception;

import lombok.Getter;

@Getter
public class EmailException extends RuntimeException{
    private String field;

    public EmailException(String field, String message) {
        super(message);
        this.field = field;
    }
}
