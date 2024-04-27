package com.project.virtualteacher.exception_handling.exceptions;

import org.springframework.http.HttpStatus;

public class UnsupportedDeleteCourseException extends VirtualTeacherCustomExceptions{
    public UnsupportedDeleteCourseException(String message) {
        super(message);
        super.setHttpStatus(HttpStatus.FORBIDDEN);
    }
}
