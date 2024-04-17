package com.example.memic.common.exception;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler
    public ResponseEntity<String> handleHttpException(HttpException e, HttpServletRequest request) {
        doLogging(e, request);
        return ResponseEntity.status(e.getStatus())
                             .body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> handleExpiredJwt(ExpiredJwtException e, HttpServletRequest request) {
        doLogging(e, request);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                             .body("토큰이 만료되었습니다");
    }

    @ExceptionHandler
    public ResponseEntity<String> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpServletRequest request) {
        doLogging(e, request);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> handleException(Exception e, HttpServletRequest request) {
        doLogging(e, request);
        return ResponseEntity.internalServerError()
                             .body("서버 내부 에러");
    }

    private void doLogging(final Exception e, HttpServletRequest request) {
        final String stackTrace = Arrays.stream(e.getStackTrace())
                                        .sequential()
                                        .map(StackTraceElement::toString)
                                        .collect(Collectors.joining("\n"));
        String formattedLog = String.format(
                """
                requestURI: %s
                message: %s
                stackTrace: %s
                """,
                request.getRequestURI(),
                e.getMessage(),
                stackTrace
        );
        logger.error(formattedLog);
    }
}
