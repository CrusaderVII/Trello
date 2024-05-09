package org.itone.trello.taskservice.util.exception.board;

import org.itone.trello.taskservice.util.exception.NotFoundException;

public class NoSuchBoardException extends NotFoundException {
    private static final String PREFIX = "There is no board with %s";

    public NoSuchBoardException(String message) {
        super(String.format(PREFIX, message));
    }
}
