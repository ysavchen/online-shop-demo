package com.mycompany.online_shop_backend.controllers.handler;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.time.Instant;

@Data
public class ErrorResponse implements Serializable {

    private final String timestamp;
    private final int status;
    private final String error;
    private final String message;
    private final String path;

    public ErrorResponse(HttpStatus httpStatus, String message, String path) {
        this.timestamp = Instant.now().toString();
        this.status = httpStatus.value();
        this.error = httpStatus.getReasonPhrase();
        this.message = message;
        this.path = path;
    }
}
