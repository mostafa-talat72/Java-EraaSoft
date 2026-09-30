package com.tasklec11.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        return buildResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation failed",
                errors
        );
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<Map<String, Object>> handleUserException(
            UserException ex) {

        Map<String, String> errors = new HashMap<>();

        errors.put(ex.getField(), ex.getMessage());

        return buildResponse(
                HttpStatus.BAD_REQUEST.value(),
                "User operation failed",
                errors
        );
    }

    @ExceptionHandler(PostException.class)
    public ResponseEntity<Map<String, Object>> handlePostException(
            PostException ex) {

        Map<String, String> errors = new HashMap<>();

        errors.put(ex.getField(), ex.getMessage());

        return buildResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Post operation failed",
                errors
        );
    }

    private ResponseEntity<Map<String, Object>> buildResponse(
            int status,
            String message,
            Map<String, String> errors) {

        Map<String, Object> response = new HashMap<>();

        response.put("status", status);
        response.put("message", message);
        response.put("errors", errors);

        return ResponseEntity
                .status(status)
                .body(response);
    }
}
