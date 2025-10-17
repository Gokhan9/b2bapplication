package com.gokhancomert.b2bapplication.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleResourceNotFoundException(ResourceNotFoundException exception) {
        // Hata mesajını içeren bir Map oluşturur
        Map<String, String> errorResponse = Map.of(
                "error", exception.getMessage(),
                "status", HttpStatus.NOT_FOUND.toString()
        );
        // Bu Map'i ve 404 Not Found durum kodunu içeren bir ResponseEntity döndürür.
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
