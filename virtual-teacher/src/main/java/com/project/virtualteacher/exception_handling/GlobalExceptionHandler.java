package com.project.virtualteacher.exception_handling;

import com.project.virtualteacher.exception_handling.error_message.ErrorMessage;
import com.project.virtualteacher.exception_handling.exceptions.VirtualTeacherCustomExceptions;
import com.project.virtualteacher.exception_handling.responce_error_model.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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

    @ExceptionHandler()
    public ResponseEntity<ErrorResponse> handleIncorrectDateFormat(DateTimeParseException e, WebRequest request){
        ErrorResponse error = new ErrorResponse();
        error.setStatusCode(HttpStatus.BAD_REQUEST.value());
        error.setMessage(ErrorMessage.INCORRECT_DATE_FORMAT);
        error.setDescription(request.getDescription(false));
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler()
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(HandlerMethodValidationException ex  , WebRequest request) {
        ErrorResponse error = new ErrorResponse();
        error.setStatusCode(HttpStatus.BAD_REQUEST.value());
        error.setMessage(ex.getAllValidationResults().get(0).getResolvableErrors().get(0).getDefaultMessage());
        error.setDescription(request.getDescription(false));
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler()
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(MethodArgumentNotValidException ex  , WebRequest request) {
        ErrorResponse error = new ErrorResponse();
        error.setStatusCode(HttpStatus.BAD_REQUEST.value());
        error.setMessage(ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        error.setDescription(request.getDescription(false));
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
