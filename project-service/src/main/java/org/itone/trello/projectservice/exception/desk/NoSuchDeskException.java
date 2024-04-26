package org.itone.trello.projectservice.exception.desk;

public class NoSuchDeskException extends RuntimeException{
    private static final String PREFIX = "There is no desk with %s";

    public NoSuchDeskException(String message) {
        super(String.format(PREFIX, message));
    }
}
