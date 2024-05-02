package org.itone.trello.projectservice.util.exception.desk;

import org.itone.trello.projectservice.util.exception.NotFoundException;

public class NoSuchDeskException extends NotFoundException {
    private static final String PREFIX = "There is no desk with %s";

    public NoSuchDeskException(String message) {
        super(String.format(PREFIX, message));
    }
}
