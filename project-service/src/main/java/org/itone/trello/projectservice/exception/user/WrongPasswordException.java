package org.itone.trello.projectservice.exception.user;

public class WrongPasswordException extends RuntimeException{
    private static final String PREFIX = "wrong password";

    public WrongPasswordException () {
        super(PREFIX);
    }
}
