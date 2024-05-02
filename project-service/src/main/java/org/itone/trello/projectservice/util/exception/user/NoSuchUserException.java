package org.itone.trello.projectservice.util.exception.user;

import org.itone.trello.projectservice.util.exception.NotFoundException;

public class NoSuchUserException extends NotFoundException {
    private static final String PREFIX = "There is no user with %s";

    public NoSuchUserException (String message) {
        super(String.format(PREFIX, message));
    }
}
