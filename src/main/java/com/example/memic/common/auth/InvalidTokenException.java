package com.example.memic.common.auth;

import com.example.memic.common.exception.HttpException;
import org.springframework.http.HttpStatus;

public class InvalidTokenException extends HttpException {

    public InvalidTokenException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
