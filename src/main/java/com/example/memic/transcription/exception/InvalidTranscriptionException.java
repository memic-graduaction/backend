package com.example.memic.transcription.exception;

import com.example.memic.common.exception.HttpException;
import org.springframework.http.HttpStatus;

public class InvalidTranscriptionException extends HttpException {

    public InvalidTranscriptionException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
