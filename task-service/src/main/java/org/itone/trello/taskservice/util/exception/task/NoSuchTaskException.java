package org.itone.trello.taskservice.util.exception.task;

import org.itone.trello.taskservice.util.exception.NotFoundException;

public class NoSuchTaskException extends NotFoundException {
    private static final String PREFIX = "There is no task with %s";

    public NoSuchTaskException(String message) {
        super(String.format(PREFIX, message));
    }
}
