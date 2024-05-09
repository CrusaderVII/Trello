package org.itone.trello.taskservice.util.exception.user;

public class InvalidDataException extends RuntimeException{
    public InvalidDataException(String message) {
        super(message);
    }
}
