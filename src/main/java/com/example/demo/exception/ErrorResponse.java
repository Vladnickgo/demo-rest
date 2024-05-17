package com.example.demo.exception;

public class ErrorResponse {
    private final String message;

    private final Integer errorCode;

    public ErrorResponse(String message, Integer errorCode) {
        this.message = message;
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public Integer getErrorCode() {
        return errorCode;
    }
}
