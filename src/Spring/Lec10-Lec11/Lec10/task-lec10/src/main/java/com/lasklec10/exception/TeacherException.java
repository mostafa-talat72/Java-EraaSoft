package com.lasklec10.exception;

import lombok.Getter;

@Getter
public class TeacherException extends RuntimeException{

    private String field;

    public TeacherException(String field, String message) {
        super(message);
        this.field = field;
    }
}
