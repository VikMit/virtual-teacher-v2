package com.project.virtualteacher.exception_handling.exceptions;

import org.springframework.http.HttpStatus;

public class UnAuthorizeException extends VirtualTeacherCustomExceptions{
    public UnAuthorizeException(String message) {
        super(message);
        super.setHttpStatus(HttpStatus.FORBIDDEN);
    }

    public UnAuthorizeException(String message, String placeHolder) {
        super(message, placeHolder);
        super.setHttpStatus(HttpStatus.FORBIDDEN);
    }

    public UnAuthorizeException(String message, int placeHolder) {
        super(message, placeHolder);
        super.setHttpStatus(HttpStatus.FORBIDDEN);
    }
}
