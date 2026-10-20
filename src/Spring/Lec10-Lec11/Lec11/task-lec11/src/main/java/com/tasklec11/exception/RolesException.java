package com.tasklec11.exception;

import lombok.Getter;

@Getter
public class RolesException extends RuntimeException{

    private String field;

    public RolesException(String field, String message) {
        super(message);
        this.field = field;
    }
}
