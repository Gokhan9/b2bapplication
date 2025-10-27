package com.gokhancomert.b2bapplication.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 404 - Not Found
     * @param exception
     */
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

    /**
     * 400 - Bad Request
     * @param exception
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException exception) {
        Map<String, String> validationErrorResponse = new HashMap<>();

        // Exception içindeki tüm alan hatalarını dön..
        exception.getBindingResult().getFieldErrors().forEach(fieldError -> {
            // Hangi alanda hata var (error.getField())
            // DTO'da belirlediğimiz mesajı (error.getDefaultMessage())
            validationErrorResponse.put(fieldError.getField(), fieldError.getDefaultMessage());
        });

        Map<String, Object> errorResponse = Map.of(
                "message", "Girdi verileri doğrulanamadı!",
                "errors", validationErrorResponse,
                "status", HttpStatus.BAD_REQUEST.toString()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * 401 - Unauthorized
     * @param exception
     */
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleInvalidCredentialsException(InvalidCredentialsException exception) {
        Map<String, String> invalidCredentialsErrorResponse = Map.of(
                "error", exception.getMessage(),
                "status", HttpStatus.UNAUTHORIZED.toString()
        );

        return new ResponseEntity<>(invalidCredentialsErrorResponse, HttpStatus.UNAUTHORIZED);
    }
}
