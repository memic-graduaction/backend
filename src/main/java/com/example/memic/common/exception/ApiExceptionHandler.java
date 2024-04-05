package com.example.memic.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<String> handleHttpException(HttpException e, HttpServletRequest request) {
       return ResponseEntity.status(e.getStatus())
                             .body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> handleException(Exception e, HttpServletRequest request) {
        return ResponseEntity.internalServerError()
                             .body(e.getMessage());
    }
}
