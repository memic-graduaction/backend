package com.example.memic.transcription.exception;

import com.example.memic.common.exception.HttpException;
import org.springframework.http.HttpStatus;

public class Mp4ExtractException extends HttpException {

    public Mp4ExtractException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }
}
