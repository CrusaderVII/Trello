package org.itone.trello.authservice.repository.exception;

public class InvalidEmailException extends RuntimeException{
    private static final String PREFIX = "Invalid email";
    public InvalidEmailException () {
        super(PREFIX);
    }
}
