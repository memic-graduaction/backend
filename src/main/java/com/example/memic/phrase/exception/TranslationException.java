package com.example.memic.phrase.exception;

import com.example.memic.common.exception.HttpException;
import org.springframework.http.HttpStatus;

public class TranslationException extends HttpException {

    public TranslationException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }
}
