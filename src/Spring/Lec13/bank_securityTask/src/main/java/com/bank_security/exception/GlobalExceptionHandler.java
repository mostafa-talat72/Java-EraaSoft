package com.bank_security.exception;

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

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Map<String, Object>> handleCustomException(CustomException ex){

        Map<String, String> errors = new HashMap<>();

        errors.put(ex.getExceptionType(), ex.getMessage());

        Map<String, Object> response = new HashMap<>();

        return buildResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Error",
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
