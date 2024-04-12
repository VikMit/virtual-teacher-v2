package com.project.virtualteacher.exception_handling.exceptions;

import org.springframework.http.HttpStatus;

public class EmailExistException extends VirtualTeacherCustomExceptions{

    public EmailExistException(String message) {
        super(message);
        super.setHttpStatus(HttpStatus.BAD_REQUEST);
    }

    public EmailExistException(String message, String placeHolder ) {
        super(message, placeHolder);
        super.setHttpStatus(HttpStatus.BAD_REQUEST);
    }
}
