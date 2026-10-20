package com.bank_security.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private String exceptionType;


    public CustomException(String exceptionType, String message) {
        super(message);
        this.exceptionType = exceptionType;
    }
}
