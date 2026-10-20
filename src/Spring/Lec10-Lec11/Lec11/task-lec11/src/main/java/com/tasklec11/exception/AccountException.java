package com.tasklec11.exception;

import lombok.Getter;

@Getter
public class AccountException extends RuntimeException{

    private String field;

    public AccountException(String field, String message) {
        super(message);
        this.field = field;
    }
}
