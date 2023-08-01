package com.sl.palabras.services.exceptions;

public class UserNotFoundException extends Exception {
    public UserNotFoundException() {
        super();
    }

    public UserNotFoundException(String username) {
        super(String.format("user with username '%s' not found", username));
    }
}