package com.example.invc_proj.exceptions;

import lombok.Data;

import java.time.Instant;

@Data
public class ApiError {
    private String timestamp;
    private int status;
    private String error;
    private String message;
    private String path;

    public ApiError(int status, String error, String message, String path) {
        this.timestamp = Instant.now().toString();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

}
