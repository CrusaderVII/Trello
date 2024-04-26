package org.itone.trello.projectservice.service;

import org.itone.trello.projectservice.exception.user.InvalidDataException;
import org.itone.trello.projectservice.exception.user.InvalidEmailException;
import org.itone.trello.projectservice.exception.user.InvalidPasswordException;
import org.itone.trello.projectservice.model.User;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class UserValidationService {

    private static final Pattern EMAIL_FORMAT_PATTERN = Pattern.compile("^.+@[a-z]+\\.(com|ru)$");
    private static final Pattern PASSWORD_FORMAT_PATTERN = Pattern.compile("^[a-zA-z0-9]{5,20}$");

    //return true only in case when both email and password are valid
    public boolean validate(User user) throws InvalidDataException {
        return  validateEmail(user.getEmail()) &&
                validatePassword(user.getPassword());
    }

    //check if email of a user being created is valid
    private boolean validateEmail(String email) throws InvalidEmailException {
        if (EMAIL_FORMAT_PATTERN.matcher(email).matches()) return true;
        else throw new InvalidEmailException();
    }

    //check if password of a user being created is valid
    //(contains from 5 to 20 latin characters in any register or digits)
    private boolean validatePassword(String password) throws InvalidPasswordException{
        if (PASSWORD_FORMAT_PATTERN.matcher(password).matches()) return true;
        else throw new InvalidPasswordException();
    }
}
