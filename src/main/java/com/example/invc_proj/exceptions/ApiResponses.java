package com.example.invc_proj.exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.time.Instant;

public final class ApiResponses {

    private ApiResponses() {}

    public static <T> ResponseEntity<ApiResponse<T>> ok(T data) {
        return ok(data, null);
    }

    public static <T> ResponseEntity<ApiResponse<T>> ok(T data, String message) {
        return build(HttpStatus.OK, data, message, null, null);
    }

    public static <T> ResponseEntity<ApiResponse<T>> ok(String message) {
        return build(HttpStatus.OK, null, message, null, null);
    }

    public static <T> ResponseEntity<ApiResponse<T>> created(T data) {
        return created(data, null);
    }

    public static <T> ResponseEntity<ApiResponse<T>> created(T data, String message) {
        return build(HttpStatus.CREATED, data, message, null, null);
    }

    public static <T> ResponseEntity<ApiResponse<T>> created(String message) {
        return build(HttpStatus.CREATED, null, message, null, null);
    }

    public static <T> ResponseEntity<ApiResponse<T>> error(
            HttpStatus httpStatus,
            String message,
            String path) {
        return error(httpStatus, null, message, path);
    }

    public static <T> ResponseEntity<ApiResponse<T>> error(
            HttpStatus httpStatus,
            String errorCode,
            String message,
            String path) {

        String finalMessage = errorCode == null ? message : "[" + errorCode + "] " + message;
        return build(httpStatus, null, finalMessage, Instant.now().toString(), path);
    }

    private static <T> ResponseEntity<ApiResponse<T>> build(
            HttpStatus httpStatus,
            T data,
            String message,
            String timestamp,
            String path) {

        ApiResponse<T> body = ApiResponse.<T>builder()
                .status(httpStatus.value())
                .statusName(httpStatus.getReasonPhrase())
                .data(data)
                .message(message)
                .timestamp(timestamp)
                .path(path)
                .build();

        return ResponseEntity.status(httpStatus).body(body);
    }
}