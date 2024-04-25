package org.itone.trello.projectservice.exception.user;

public class NoSuchUserException extends RuntimeException{
    private static final String PREFIX = "There is no user with %s";

    public NoSuchUserException (String message) {
        super(String.format(PREFIX, message));
    }
}
