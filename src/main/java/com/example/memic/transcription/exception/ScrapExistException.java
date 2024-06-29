package com.example.memic.transcription.exception;

import com.example.memic.common.exception.HttpException;
import org.springframework.http.HttpStatus;

public class ScrapExistException extends HttpException {

    public ScrapExistException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
