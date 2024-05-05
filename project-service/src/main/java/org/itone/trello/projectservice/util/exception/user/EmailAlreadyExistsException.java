package org.itone.trello.projectservice.util.exception.user;

import org.springframework.dao.DataIntegrityViolationException;

public class EmailAlreadyExistsException extends DataIntegrityViolationException {
    private static final String PREFIX = "Email is already exists";
    public EmailAlreadyExistsException() {
        super(PREFIX);
    }
}
