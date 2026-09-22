package com.tasklec8.exception;

import lombok.Getter;

@Getter
public class CourseException extends RuntimeException{

    private String field;


    public CourseException(String field, String message) {
        super(message);
        this.field = field;
    }
}
