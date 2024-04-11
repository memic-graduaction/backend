package com.example.memic.phrase.exception;

import com.example.memic.common.exception.HttpException;
import org.springframework.http.HttpStatus;

public class InvalidPhraseException extends HttpException {

    public InvalidPhraseException(final String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
