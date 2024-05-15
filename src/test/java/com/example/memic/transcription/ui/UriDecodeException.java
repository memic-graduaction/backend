package com.example.memic.transcription.ui;

import com.example.memic.common.exception.HttpException;
import org.springframework.http.HttpStatus;

public class UriDecodeException extends HttpException {
    public UriDecodeException(final String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
