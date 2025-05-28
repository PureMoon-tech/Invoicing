package com.example.invc_proj.exceptions;


public class InvalidPasswordLengthException extends RuntimeException {
    public InvalidPasswordLengthException(String message) {
        super(message);
    }
}
