package org.itone.trello.projectservice.exception.user;

public class InvalidEmailException extends InvalidDataException{
    private static final String PREFIX = "Invalid email";
    public InvalidEmailException () {
        super(PREFIX);
    }
}
