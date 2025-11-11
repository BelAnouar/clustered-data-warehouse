package org.hotelreservation.clustereddatawarehouse.exception;

import lombok.extern.slf4j.Slf4j;
import org.hotelreservation.clustereddatawarehouse.validation.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidation(MethodArgumentNotValidException ex) {
        var errors = ex.getBindingResult().getAllErrors().stream()
                .map(e -> new ErrorResponse.ValidationError(
                        e.getObjectName(),
                        e.getDefaultMessage()
                ))
                .toList();

        return new ErrorResponse(LocalDateTime.now(), 400,
                "Validation Failed", "Invalid request data", errors);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDuplicate(IllegalArgumentException ex) {
        return new ErrorResponse(LocalDateTime.now(), 409,
                "Duplicate Deal", ex.getMessage(), null);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGeneric(Exception ex) {
        log.error("Error: ", ex);
        return new ErrorResponse(LocalDateTime.now(), 500,
                "Internal Error", "An unexpected error occurred", null);
    }
}