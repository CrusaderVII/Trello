package org.itone.trello.authservice.repository.service;

import org.itone.trello.authservice.repository.exception.InvalidEmailException;
import org.itone.trello.authservice.repository.exception.InvalidPasswordException;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class DataValidationService {

    private static final Pattern EMAIL_FORMAT_PATTERN = Pattern.compile("^(.+)@(\\S+)\\.(com|ru)$");
    private static final Pattern PASSWORD_FORMAT_PATTERN = Pattern.compile("^[a-zA-Z0-9]{5,30}$");

    public boolean validateEmail (String email) throws InvalidEmailException{
        if (EMAIL_FORMAT_PATTERN.matcher(email).matches()) return true;
        else throw new InvalidEmailException();
    }

    public boolean validatePassword (String password) throws InvalidPasswordException{
        if (PASSWORD_FORMAT_PATTERN.matcher(password).matches()) return true;
        else throw new InvalidPasswordException();
    }

}
