package org.itone.trello.projectservice.util.exception.user;

public class InvalidPasswordException extends InvalidDataException{
    private static final String PREFIX = "Invalid password. It should be between 5 and 30 symbols long" +
            " and should contain only latin letters and digits";
    public InvalidPasswordException () {
        super(PREFIX);
    }
}
