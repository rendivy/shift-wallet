package com.example.shiftwallet.interceptor;

import com.example.shiftwallet.dao.model.ErrorDetailsResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionInterceptor {

    @ExceptionHandler
    public ResponseEntity<ErrorDetailsResponse> handleException(Exception exception) {

        var exceptionMessage = exception
                .getMessage();

        var error = ErrorDetailsResponse
                .builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(exceptionMessage)
                .build();

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
