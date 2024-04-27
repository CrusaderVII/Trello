package org.itone.trello.projectservice.exception.task;

public class NoSuchTaskException extends RuntimeException{
    private static final String PREFIX = "There is no task with %s";

    public NoSuchTaskException(String message) {
        super(String.format(PREFIX, message));
    }
}
