package org.itone.trello.projectservice.service;

import org.itone.trello.projectservice.util.exception.user.InvalidDataException;
import org.itone.trello.projectservice.util.exception.user.InvalidEmailException;
import org.itone.trello.projectservice.util.exception.user.InvalidNameException;
import org.itone.trello.projectservice.util.exception.user.InvalidPasswordException;
import org.itone.trello.projectservice.dao.model.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class UserValidationService {

    private static final Pattern EMAIL_FORMAT_PATTERN = Pattern.compile("^\\S+@[a-z]+\\.(com|ru)$");
    private static final Pattern PASSWORD_FORMAT_PATTERN = Pattern.compile("^[a-zA-z0-9]{5,20}$");
    private static final Pattern NAME_FORMAT_PATTERN = Pattern.compile("\\S{2,20}");
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    //return true only in case when email, password and name are valid
    public boolean validate(User user) throws InvalidDataException {
        return  validateEmail(user.getEmail()) &&
                validatePassword(user.getPassword()) &&
                validateName(user.getName());
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

    //check if name of a user being created is valid
    //(contains from 2 to 20 any non-whitespace characters)
    private boolean validateName(String name) throws InvalidNameException{
        if (NAME_FORMAT_PATTERN.matcher(name).matches()) return true;
        else throw new InvalidNameException();
    }
}
