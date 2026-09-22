package com.tasklec8.exception;

import lombok.Getter;

@Getter
public class StudentException extends RuntimeException{
    private String field;

    public StudentException(String field, String message) {
        super(message);
        this.field = field;
    }
}
