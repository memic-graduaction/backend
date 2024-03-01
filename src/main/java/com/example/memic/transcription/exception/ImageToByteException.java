package com.example.memic.transcription.exception;

import com.example.memic.common.exception.HttpException;
import java.io.IOException;
import org.springframework.http.HttpStatus;

public class ImageToByteException extends HttpException {

    public ImageToByteException(IOException e) {
        super(HttpStatus.BAD_REQUEST, e.getMessage());
    }
}
