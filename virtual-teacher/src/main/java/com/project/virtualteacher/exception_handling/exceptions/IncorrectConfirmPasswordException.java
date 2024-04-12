package com.project.virtualteacher.exception_handling.exceptions;

import org.springframework.http.HttpStatus;

public class IncorrectConfirmPasswordException extends VirtualTeacherCustomExceptions{

    public IncorrectConfirmPasswordException(String message){
        super(message);
        super.setHttpStatus(HttpStatus.BAD_REQUEST);
    }

}
