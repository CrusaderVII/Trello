package org.itone.trello.authservice.repository.exception;

public class NoSuchUserException extends RuntimeException{
    private static final String PREFIX = "There is no user with name %s";

    public NoSuchUserException (String login) {
        super(String.format(PREFIX, login));
    }
}
