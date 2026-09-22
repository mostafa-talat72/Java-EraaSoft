package com.tasklec8.exception;

import lombok.Getter;

@Getter
public class InstructorException extends RuntimeException{
    private String field;


    public InstructorException(String field, String message) {
        super(message);
        this.field = field;
    }
}
