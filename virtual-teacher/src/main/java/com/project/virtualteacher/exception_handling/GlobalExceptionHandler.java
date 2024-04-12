package com.project.virtualteacher.exception_handling;

import com.project.virtualteacher.exception_handling.exceptions.VirtualTeacherCustomExceptions;
import com.project.virtualteacher.exception_handling.responce_error_model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.format.DateTimeParseException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler()
    public <E extends VirtualTeacherCustomExceptions> ResponseEntity<ErrorResponse> handleVirtualTeacherExceptions(E e, WebRequest request) {
        ErrorResponse error = new ErrorResponse();
        error.setStatusCode(e.getHttpStatus().value());
        error.setMessage(e.getMessage());
        error.setDescription(request.getDescription(false));
        return new ResponseEntity<>(error, e.getHttpStatus());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleIncorrectDateFormat(DateTimeParseException e, WebRequest request){
        ErrorResponse error = new ErrorResponse();
        error.setStatusCode(HttpStatus.BAD_REQUEST.value());
        error.setMessage(e.getMessage());
        error.setDescription(request.getDescription(false));
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

}
