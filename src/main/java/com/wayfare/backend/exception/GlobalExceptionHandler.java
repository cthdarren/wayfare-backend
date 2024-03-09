package com.wayfare.backend.exception;

import com.wayfare.backend.ResponseObject;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.security.NoSuchAlgorithmException;

@RestControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidInputException.class)
    public ResponseObject handleInvalidInputException(InvalidInputException e) {
        return new ResponseObject(false, e.getMessage().split(","));
    }
    @ExceptionHandler(NoSuchAlgorithmException.class)
    public ResponseObject handleNoSuchAlgorithmException(NoSuchAlgorithmException e) {
        return new ResponseObject(false, "Password authentication failed (check hash algorithm used)");
    }
}