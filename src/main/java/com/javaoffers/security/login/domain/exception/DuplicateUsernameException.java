package com.javaoffers.security.login.domain.exception;

public class DuplicateUsernameException extends RuntimeException {

    public static final String MESSAGE = "Username '%s' already exists";

    public DuplicateUsernameException(String username) {
        super(getMessage(username));
    }

    private static String getMessage(String username) {
        return String.format(MESSAGE, username);
    }
}