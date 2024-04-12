package com.project.virtualteacher.exception_handling.exceptions;

import org.springframework.http.HttpStatus;

public class UsernameExistException extends VirtualTeacherCustomExceptions{

    public UsernameExistException(String message) {
        super(message);
        super.setHttpStatus(HttpStatus.BAD_REQUEST);
    }

    public UsernameExistException(String message, int placeHolder) {
        super(String.format(message, placeHolder));
        super.setHttpStatus(HttpStatus.BAD_REQUEST);
    }

    public UsernameExistException(String message, String placeHolder ) {
        super(String.format(message, placeHolder));
        super.setHttpStatus(HttpStatus.BAD_REQUEST);
    }

}
