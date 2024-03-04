package com.example.memic.recognizedSentence.exception;

import com.example.memic.common.exception.HttpException;
import org.springframework.http.HttpStatus;

public class InvalidRecognizedException extends HttpException {

    public InvalidRecognizedException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
