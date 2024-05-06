package org.itone.trello.projectservice.util.exception.user;

import org.springframework.dao.DataIntegrityViolationException;

public class EmailAlreadyExistsException extends DataIntegrityViolationException {
    private static final String FORMAT = "Email %s is already exists";
    public EmailAlreadyExistsException(String email) {
        super(String.format(FORMAT, email));
    }
}
