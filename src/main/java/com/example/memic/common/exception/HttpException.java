package com.example.memic.common.exception;

import org.springframework.http.HttpStatus;

public class HttpException extends RuntimeException {

    private final HttpStatus status;
    private final String message;

    public HttpException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
