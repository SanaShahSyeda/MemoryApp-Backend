package com.spring.memory.exception;

/**
 * Custom exception for JWT token validation failures.
 * Thrown when a token is invalid, expired, unsupported, or malformed.
 */
public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String message) {
        super(message);
    }

    public InvalidTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
