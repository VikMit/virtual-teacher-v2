package com.project.virtualteacher.exception_handling.exceptions;

import org.springframework.http.HttpStatus;

public class IncorrectInputException extends VirtualTeacherCustomExceptions {

    public IncorrectInputException(String message) {
        super(message);
        super.setHttpStatus(HttpStatus.BAD_REQUEST);
    }

    public IncorrectInputException(String message, String placeHolder) {
        super(message, placeHolder);
        super.setHttpStatus(HttpStatus.BAD_REQUEST);
    }
}
