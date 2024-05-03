package com.project.virtualteacher.exception_handling.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Setter
@Getter
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

    protected VirtualTeacherCustomExceptions(String message,String placeHolderOne, String placeHolderTwo) {
        super(String.format(message,placeHolderOne,placeHolderTwo));
    }

    protected VirtualTeacherCustomExceptions(String message,String placeHolderOne, int placeHolderTwo) {
        super(String.format(message,placeHolderOne,placeHolderTwo));
    }

    protected VirtualTeacherCustomExceptions(String message,int placeHolderOne, int placeHolderTwo) {
        super(String.format(message,placeHolderOne,placeHolderTwo));
    }

}
