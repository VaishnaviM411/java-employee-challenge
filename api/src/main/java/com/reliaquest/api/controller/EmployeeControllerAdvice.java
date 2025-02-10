package com.reliaquest.api.controller;

import com.reliaquest.api.exception.HttpException;
import com.reliaquest.api.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class EmployeeControllerAdvice {

    @ExceptionHandler
    protected ResponseEntity<ErrorResponse> handleHttpException(HttpException exception) {
        log.error("Error handling web request.", exception);
        return ResponseEntity.status(exception.getStatus())
                .body(new ErrorResponse(
                        exception.getErrorMessage() != null ? exception.getErrorMessage() : exception.getMessage()));
    }

    @ExceptionHandler
    protected ResponseEntity<ErrorResponse> handleHttpException(MethodArgumentNotValidException exception) {
        log.error("Error handling web request.", exception);
        return ResponseEntity.badRequest().body(new ErrorResponse(exception.getMessage()));
    }
}
