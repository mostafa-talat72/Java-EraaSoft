package com.tasklec11.exception;

import lombok.Getter;

@Getter
public class PostException extends RuntimeException{

    private String field;


    public PostException(String field, String message) {
        super(message);
        this.field = field;
    }
}
