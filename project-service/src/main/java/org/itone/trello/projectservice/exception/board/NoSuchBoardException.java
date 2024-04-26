package org.itone.trello.projectservice.exception.board;

public class NoSuchBoardException extends RuntimeException{
    private static final String PREFIX = "There is no board with %s";

    public NoSuchBoardException(String message) {
        super(String.format(PREFIX, message));
    }
}
