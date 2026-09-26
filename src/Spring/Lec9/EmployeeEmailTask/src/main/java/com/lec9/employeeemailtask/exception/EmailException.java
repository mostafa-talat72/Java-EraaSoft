package com.lec9.employeeemailtask.exception;

import lombok.Getter;

@Getter
// Business error for email flows; handled as 400 with field detail.
public class EmailException extends RuntimeException{
    // Field name used as key in the errors map.
    private String field;

    public EmailException(String field, String message) {
        super(message);
        this.field = field;
    }
}
