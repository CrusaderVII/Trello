package org.itone.trello.authservice.repository.exception;

public class WrongPasswordException extends RuntimeException{
    private static final String PREFIX = "wrong password";

    public WrongPasswordException () {
        super(PREFIX);
    }
}
