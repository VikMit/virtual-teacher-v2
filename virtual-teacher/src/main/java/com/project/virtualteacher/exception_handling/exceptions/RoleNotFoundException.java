package com.project.virtualteacher.exception_handling.exceptions;

import org.springframework.http.HttpStatus;

public class RoleNotFoundException extends VirtualTeacherCustomExceptions{
    public RoleNotFoundException(String message) {
        super(message);
        super.setHttpStatus(HttpStatus.BAD_REQUEST);
    }

    public RoleNotFoundException(String message, String placeHolder) {
        super(message, placeHolder);
        super.setHttpStatus(HttpStatus.BAD_REQUEST);
    }

    public RoleNotFoundException(String message, int placeHolder) {
        super(message, placeHolder);
        super.setHttpStatus(HttpStatus.BAD_REQUEST);
    }
}
