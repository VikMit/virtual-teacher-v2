package com.project.virtualteacher.exception_handling.exceptions;

import org.springframework.http.HttpStatus;

public class UnAuthorizeException extends VirtualTeacherCustomExceptions{
    public UnAuthorizeException(String message) {
        super(message);
        super.setHttpStatus(HttpStatus.UNAUTHORIZED);
    }

    public UnAuthorizeException(String message, String placeHolder) {
        super(message, placeHolder);
        super.setHttpStatus(HttpStatus.UNAUTHORIZED);
    }

    public UnAuthorizeException(String message, int placeHolder) {
        super(message, placeHolder);
        super.setHttpStatus(HttpStatus.UNAUTHORIZED);
    }

    public UnAuthorizeException(String message, String placeHolderOne, String placeHoledTwo) {
        super(message, placeHolderOne,placeHoledTwo);
        super.setHttpStatus(HttpStatus.UNAUTHORIZED);
    }
    public UnAuthorizeException(String message, String placeHolderOne, int placeHoledTwo) {
        super(message, placeHolderOne,placeHoledTwo);
        super.setHttpStatus(HttpStatus.UNAUTHORIZED);
    }
}
