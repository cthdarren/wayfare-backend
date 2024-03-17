package com.wayfare.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.wayfare.backend.ResponseObject;

// This file handles exception of all requests when and exception is thrown from spring instead
// an example is the HttpMessageNotReadableException which is thrown when spring detects an
// invalid JSON body in the request

@ControllerAdvice
public class GlobalExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseObject handleHttpMessageNotReadableException(HttpMessageNotReadableException e){
        System.out.println(e.getMessage());
        return new ResponseObject(false, "Request body is missing or invalid, please check again.");
    }
    
}
