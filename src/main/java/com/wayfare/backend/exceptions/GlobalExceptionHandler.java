package com.wayfare.backend.exceptions;

import com.wayfare.backend.ResponseObject;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseObject handleIllegalArgumentException(IllegalArgumentException e) {
        ResponseObject response = new ResponseObject(false, e.getMessage());
        return response;
    }
}