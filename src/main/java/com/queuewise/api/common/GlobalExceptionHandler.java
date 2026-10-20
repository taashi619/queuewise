package com.queuewise.api.common;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ApiErrorResponse> handleValidationException(
                        MethodArgumentNotValidException exception,
                        HttpServletRequest request) {
                Map<String, String> fieldErrors = new LinkedHashMap<>();

                exception.getBindingResult()
                                .getFieldErrors()
                                .forEach(error -> fieldErrors.putIfAbsent(
                                                error.getField(),
                                                error.getDefaultMessage()));

                ApiErrorResponse response = new ApiErrorResponse(
                                Instant.now(),
                                400,
                                "One or more fields are invalid.",
                                request.getRequestURI(),
                                fieldErrors);

                return ResponseEntity.badRequest().body(response);
        }

        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<ApiErrorResponse> handleIllegalArgumentException(
                        IllegalArgumentException exception,
                        HttpServletRequest request) {
                ApiErrorResponse response = new ApiErrorResponse(
                                Instant.now(),
                                400,
                                exception.getMessage(),
                                request.getRequestURI(),
                                Map.of());

                return ResponseEntity.badRequest().body(response);
        }

        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<ApiErrorResponse> handleResourceNotFoundException(
                        ResourceNotFoundException exception,
                        HttpServletRequest request) {
                ApiErrorResponse response = new ApiErrorResponse(
                                Instant.now(),
                                404,
                                exception.getMessage(),
                                request.getRequestURI(),
                                Map.of());

                return ResponseEntity.status(404).body(response);
        }
}