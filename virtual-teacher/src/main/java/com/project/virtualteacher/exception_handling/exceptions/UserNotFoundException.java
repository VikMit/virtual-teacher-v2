package com.project.virtualteacher.exception_handling.exceptions;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends VirtualTeacherCustomExceptions {

    public UserNotFoundException(String message) {
       super(message);
       super.setHttpStatus(HttpStatus.NOT_FOUND);
    }

    public UserNotFoundException(String message,String placeHolder) {
        super(message,placeHolder);
        super.setHttpStatus(HttpStatus.NOT_FOUND);
    }

    public UserNotFoundException(String message,int placeHolder) {
        super(message,placeHolder);
        super.setHttpStatus(HttpStatus.NOT_FOUND);
    }


}
