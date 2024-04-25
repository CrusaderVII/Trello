package org.itone.trello.projectservice.exception.project;

public class NoSuchProjectException extends RuntimeException{
    private static final String PREFIX = "There is no project with %s";

    public NoSuchProjectException(String message) {
        super(String.format(PREFIX, message));
    }
}
