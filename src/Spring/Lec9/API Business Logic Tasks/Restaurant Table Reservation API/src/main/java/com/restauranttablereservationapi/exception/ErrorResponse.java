package com.restauranttablereservationapi.exception;

// Uniform JSON error body for every handled failure:
// { "code": 404, "message": "Reservation with id 9 not found" }
public record ErrorResponse(int code, String message) {
}
