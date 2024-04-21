package com.example.memic.common.exception;

import org.springframework.http.HttpStatus;

public class AuthorizationException extends HttpException {

    public AuthorizationException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
