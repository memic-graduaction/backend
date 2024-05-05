package com.example.memic.common.exception;

import com.example.memic.common.slack.InternalErrorSender;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.util.ContentCachingRequestWrapper;

@RestControllerAdvice
public class ApiExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(ApiExceptionHandler.class);

    private final InternalErrorSender InternalErrorSender;

    public ApiExceptionHandler(final InternalErrorSender InternalErrorSender) {
        this.InternalErrorSender = InternalErrorSender;
    }

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
    public ResponseEntity<String> handleException(Exception e, HttpServletRequest request) throws IOException {
        final ContentCachingRequestWrapper cachingRequest = (ContentCachingRequestWrapper) request;
        InternalErrorSender.execute(cachingRequest, e);

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
