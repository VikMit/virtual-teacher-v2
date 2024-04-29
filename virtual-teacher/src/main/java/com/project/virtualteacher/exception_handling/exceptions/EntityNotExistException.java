package com.project.virtualteacher.exception_handling.exceptions;

import org.springframework.http.HttpStatus;

public class EntityNotExistException extends VirtualTeacherCustomExceptions{

    public EntityNotExistException(String message) {
        super(message);
        super.setHttpStatus(HttpStatus.NOT_FOUND);

    }

    public EntityNotExistException(String message, String placeHolder) {
        super(message, placeHolder);
        super.setHttpStatus(HttpStatus.NOT_FOUND);

    }

    public EntityNotExistException(String message, int placeHolder) {
        super(message, placeHolder);
        super.setHttpStatus(HttpStatus.NOT_FOUND);

    }
}
