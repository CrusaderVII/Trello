package org.itone.trello.taskservice.util.exception.user;

public class InvalidNameException extends InvalidDataException {
    private static final String PREFIX = "Invalid name";
    public InvalidNameException () {
        super(PREFIX);
    }
}
