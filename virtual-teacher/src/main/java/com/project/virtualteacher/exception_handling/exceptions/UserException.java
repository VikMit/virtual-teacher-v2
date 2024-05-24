package com.project.virtualteacher.exception_handling.exceptions;

import org.springframework.http.HttpStatus;

public class UserException extends VirtualTeacherCustomExceptions{
    public UserException(String message) {
        super(message);
        super.setHttpStatus(HttpStatus.NOT_FOUND);
    }
}
