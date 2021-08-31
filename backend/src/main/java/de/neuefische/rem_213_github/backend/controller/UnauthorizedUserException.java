package de.neuefische.rem_213_github.backend.controller;

public class UnauthorizedUserException extends RuntimeException {

    public UnauthorizedUserException(String message) {
        super(message);
    }
}
