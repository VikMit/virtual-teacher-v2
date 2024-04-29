package com.project.virtualteacher.exception_handling.exceptions;

import org.springframework.http.HttpStatus;

public class UnsupportedOperationException extends VirtualTeacherCustomExceptions{
    public UnsupportedOperationException(String message) {
        super(message);
        super.setHttpStatus(HttpStatus.FORBIDDEN);
    }
}
