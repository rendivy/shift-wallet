package com.example.shiftwallet.interceptor;

import com.example.shiftwallet.dao.model.ErrorDetailsResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ConstraintViolationHandler {
    @ExceptionHandler
    public ResponseEntity<ErrorDetailsResponse> handleConstraintViolationException(ConstraintViolationException exception) {

        var firstViolation = exception
                .getConstraintViolations()
                .iterator()
                .next();

        var exceptionMessage = firstViolation
                .getMessageTemplate();

        var error = ErrorDetailsResponse
                .builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(exceptionMessage)
                .build();

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
