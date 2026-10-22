package com.appointmentbookingapi.exception;

// Uniform JSON error body for every handled failure:
// { "code": 404, "message": "Doctor with id 9 not found" }
public record ErrorResponse(int code, String message) {
}
