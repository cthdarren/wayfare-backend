//package com.wayfare.backend.exceptions;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.ErrorResponse;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//@RestControllerAdvice
//public class GlobalExceptionHandler extends  {
//
//
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ErrorResponse handleException(MethodArgumentNotValidException e){
//        e.getBindingResult().getAllErrors();
//    }
//
//    @ExceptionHandler(JsonMappingException.class)
//    public ResponseEntity<ErrorMessage> handleJsonMappingException(JsonMappingException e){
//    }

//}