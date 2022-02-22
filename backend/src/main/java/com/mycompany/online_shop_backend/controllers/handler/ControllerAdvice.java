package com.mycompany.online_shop_backend.controllers.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> unauthorizedException(UsernameNotFoundException ex, HttpServletRequest request) {
        var errorResponse = new ErrorResponse(
                HttpStatus.UNAUTHORIZED, ex.getMessage(), request.getRequestURI()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> exception(Exception ex, HttpServletRequest request) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        ResponseStatus responseStatus = ex.getClass().getAnnotation(ResponseStatus.class);
        if (responseStatus != null) {
            httpStatus = responseStatus.value();
        }

        switch (httpStatus) {
            case INTERNAL_SERVER_ERROR -> log.error("", ex);
            case BAD_REQUEST -> log.debug("400 Bad Request - " + ex);
            case UNAUTHORIZED -> log.debug("Unauthorized");
            default -> log.warn("", ex);
        }

        var errorResponse = new ErrorResponse(
                httpStatus, ex.getMessage(), request.getRequestURI()
        );
        return new ResponseEntity<>(errorResponse, httpStatus);
    }
}
