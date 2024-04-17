package com.example.memic.member.exception;

import com.example.memic.common.exception.HttpException;
import org.springframework.http.HttpStatus;

public class InvalidMemberException extends HttpException {

    public InvalidMemberException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
