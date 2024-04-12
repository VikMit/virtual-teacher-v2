package com.project.virtualteacher.exception_handling.error_message;

public final class ErrorMessage {
    public static final String USER_NOT_FOUND = "User with ID = %d not found.";
    public static final String USERNAME_EXIST = "User with username: '%s' already exist.";
    public static final String EMAIL_EXIST = "Email: %s already exist";
    public static final String INCORRECT_CONFIRM_PASSWORD = "Password and confirm password does not match";

    public static final String USER_ID_NOT_VALID_ERROR = "User ID must be positive";

    public static final String ROLE_NAME_NOT_FOUND = "Role with name: %s, not found";
    public static final String ROLE_ID_NOT_FOUND = "Role with ID: %d, not found";
}
