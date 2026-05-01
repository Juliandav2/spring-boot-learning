package com.julian.spring_demo.exception;

import com.julian.spring_demo.model.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashMap;
import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler (ProductNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound (ProductNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "timestamp", LocalDateTime.now(),
                "status", 404,
                "error", e.getMessage()));
    }

    @ExceptionHandler (IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequest (IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "timestamp", LocalDateTime.now(),
                "status", 400,
                "error", e.getMessage()));
    }

    @ExceptionHandler (MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation (MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String field = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(field, message);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of
                ("status", 400, "errors", errors));
    }

    @ExceptionHandler (CategoryNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFoundCategory (CategoryNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "timestamp", LocalDateTime.now(),
                "status", 404,
                "error", e.getMessage()));
    }

    @ExceptionHandler (SupplierNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFoundSupplier (SupplierNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "timestamp", LocalDateTime.now(),
                "status", 404,
                "error", e.getMessage()));
    }

    @ExceptionHandler (TagNotFoundException.class)
    public ResponseEntity <Map<String, Object>> handleNotFoundTag (TagNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "timestamp", LocalDateTime.now(),
                "status", 404,
                "error", e.getMessage()));
    }

}
