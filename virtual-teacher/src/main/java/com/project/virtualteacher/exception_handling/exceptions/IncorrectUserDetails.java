package com.project.virtualteacher.exception_handling.exceptions;

import org.springframework.http.HttpStatus;

public class IncorrectUserDetails extends VirtualTeacherCustomExceptions{

    public IncorrectUserDetails(String message) {
        super(message);
        super.setHttpStatus(HttpStatus.BAD_REQUEST);
    }

    public IncorrectUserDetails(String message, String placeHolder) {
        super(message,placeHolder);
        super.setHttpStatus(HttpStatus.BAD_REQUEST);
    }
}
