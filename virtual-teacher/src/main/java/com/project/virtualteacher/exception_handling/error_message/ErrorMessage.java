package com.project.virtualteacher.exception_handling.error_message;

public final class ErrorMessage {
    public static final String USER_ID_NOT_FOUND = "User with ID = %d not found.";
    public static final String USERNAME_EXIST = "User with username: '%s' already exist.";
    public static final String EMAIL_EXIST = "Email: %s already exist.";
    public static final String INCORRECT_CONFIRM_PASSWORD = "Password and confirm password does not match.";
    public static final String USER_ID_NOT_VALID = "User ID must be positive.";
    public static final String ROLE_NAME_NOT_FOUND = "Role with name: %s, not found.";
    public static final String ROLE_ID_NOT_FOUND = "Role with ID: %d, not found.";
    public static final String USER_NOT_RESOURCE_OWNER = "Only owner of the resource have permits to modify or delete.";
    public static final String USERNAME_NOT_FOUND = "User with username: %s not found.";
    public static final String COURSE_TITLE_EXIST = "Course with title: %s already exit.";
    public static final String COURSE_WITH_ID_NOT_FOUND = "Course with ID: %d not found.";
    public static final String COURSE_WITH_TITLE_NOT_FOUND = "Course with title: %s not found.";
    public static final String PUBLIC_COURSE_WITH_ID_NOT_FOUND = "Public course with ID: %d not found.";
    public static final String PUBLIC_COURSE_WITH_TITLE_NOT_FOUND = "Public course with title: %s not found.";
    public static final String NOT_COURSE_CREATOR_ERROR = "Only creator of the course can modify it.";
    public static final String INCORRECT_DATE_FORMAT = "Incorrect date format, accepted format 'yyyy-MM-dd'";
}
