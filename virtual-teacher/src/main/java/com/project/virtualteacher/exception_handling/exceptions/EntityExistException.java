package com.project.virtualteacher.exception_handling.exceptions;

import org.springframework.http.HttpStatus;

public class EntityExistException extends VirtualTeacherCustomExceptions{

    public EntityExistException(String message) {
        super(message);
        super.setHttpStatus(HttpStatus.BAD_REQUEST);
    }

    public EntityExistException(String message, int placeHolder) {
        super(String.format(message, placeHolder));
        super.setHttpStatus(HttpStatus.BAD_REQUEST);
    }

    public EntityExistException(String message, String placeHolder ) {
        super(String.format(message, placeHolder));
        super.setHttpStatus(HttpStatus.BAD_REQUEST);
    }

}
