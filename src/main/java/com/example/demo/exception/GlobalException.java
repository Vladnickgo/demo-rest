package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static com.example.demo.exception.ErrorCode.USER_IS_NULL;

@ControllerAdvice
public class GlobalException {
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleDataIntegrityViolationException(Exception exception) {
        return new ErrorResponse(exception.getMessage(), USER_IS_NULL);
    }
}
