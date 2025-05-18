package lk.binuri.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        ex.getGlobalErrors().forEach(error -> handleGlobalValidationErrors(errors, error));

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomErrorException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(CustomErrorException ex) {
        Map<String, String> errors = ex.getErrors();
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadLoginException.class)
    public ResponseEntity<Map<String, String>> handleLoginExceptions(BadLoginException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error",ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.UNAUTHORIZED);
    }

    private static void handleGlobalValidationErrors(Map<String, String> errors, ObjectError error) {
        if (error.getCode().equals(PasswordMatches.class.getSimpleName())) {
            errors.put("confirmPassword", error.getDefaultMessage());
            return;
        }

        errors.put("global", error.getDefaultMessage());
    }
}