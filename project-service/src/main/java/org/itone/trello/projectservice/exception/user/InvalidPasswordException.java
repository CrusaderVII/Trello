package org.itone.trello.projectservice.exception.user;

public class InvalidPasswordException extends RuntimeException{
    private static final String PREFIX = "Invalid password. It should be between 5 and 30 symbols long\n" +
            " and should contain only latin letters and digits";
    public InvalidPasswordException () {
        super(PREFIX);
    }
}
