package com.productdiscountapi.exception;

// Uniform JSON error body for every handled failure:
// { "code": 404, "message": "Product with id 9 not found" }
public record ErrorResponse(int code, String message) {
}
