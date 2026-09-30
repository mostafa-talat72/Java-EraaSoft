package com.tasklec11.exception;

import lombok.Getter;

@Getter
public class UserException extends RuntimeException{

    private String field;

    public UserException(String field, String message) {
        super(message);
        this.field = field;
    }
}
