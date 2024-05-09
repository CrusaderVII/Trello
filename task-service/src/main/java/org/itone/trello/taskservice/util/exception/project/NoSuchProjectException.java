package org.itone.trello.taskservice.util.exception.project;

import org.itone.trello.taskservice.util.exception.NotFoundException;

public class NoSuchProjectException extends NotFoundException {
    private static final String PREFIX = "There is no project with %s";

    public NoSuchProjectException(String message) {
        super(String.format(PREFIX, message));
    }
}
