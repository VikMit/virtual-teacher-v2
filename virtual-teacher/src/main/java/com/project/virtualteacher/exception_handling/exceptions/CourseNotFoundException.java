package com.project.virtualteacher.exception_handling.exceptions;

import org.springframework.http.HttpStatus;

public class CourseNotFoundException extends VirtualTeacherCustomExceptions{

    public CourseNotFoundException(String message) {
        super(message);
        super.setHttpStatus(HttpStatus.BAD_REQUEST);

    }

    public CourseNotFoundException(String message, String placeHolder) {
        super(message, placeHolder);
        super.setHttpStatus(HttpStatus.BAD_REQUEST);

    }

    public CourseNotFoundException(String message, int placeHolder) {
        super(message, placeHolder);
        super.setHttpStatus(HttpStatus.BAD_REQUEST);

    }
}
