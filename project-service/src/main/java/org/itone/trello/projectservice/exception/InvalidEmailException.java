package org.itone.trello.projectservice.exception;

public class InvalidEmailException extends RuntimeException{
    private static final String PREFIX = "Invalid email";
    public InvalidEmailException () {
        super(PREFIX);
    }
}
