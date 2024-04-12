package com.project.virtualteacher.exception_handling.exceptions;

import org.springframework.http.HttpStatus;

public abstract class VirtualTeacherCustomExceptions extends RuntimeException{
    private  HttpStatus httpStatus;

    protected VirtualTeacherCustomExceptions(String message) {
        super(message);
    }

    protected VirtualTeacherCustomExceptions(String message,String placeHolder) {
        super(String.format(message,placeHolder));
    }
    protected VirtualTeacherCustomExceptions(String message,int placeHolder) {
        super(String.format(message,placeHolder));
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

}
