package com.project.virtualteacher.exception_handling.exceptions;

import org.springframework.http.HttpStatus;

public class CourseTitleExistException extends VirtualTeacherCustomExceptions{
    public CourseTitleExistException(String message) {
        super(message);
        setHttpStatus(HttpStatus.BAD_REQUEST);
    }

    public CourseTitleExistException(String message, String placeHolder) {
        super(message, placeHolder);
        setHttpStatus(HttpStatus.BAD_REQUEST);

    }

    public CourseTitleExistException(String message, int placeHolder) {
        super(message, placeHolder);
        setHttpStatus(HttpStatus.BAD_REQUEST);

    }
}
