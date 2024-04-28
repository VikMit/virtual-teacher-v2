package com.project.virtualteacher.exception_handling.exceptions;

import org.springframework.http.HttpStatus;

public class RoleException extends VirtualTeacherCustomExceptions{
    public RoleException(String message) {
        super(message);
        super.setHttpStatus(HttpStatus.BAD_REQUEST);
    }

    public RoleException(String message, String placeHolder) {
        super(message, placeHolder);
        super.setHttpStatus(HttpStatus.BAD_REQUEST);
    }

    public RoleException(String message, int placeHolder) {
        super(message, placeHolder);
        super.setHttpStatus(HttpStatus.BAD_REQUEST);
    }
}
