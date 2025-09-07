package com.example.pandemic.userinterface.http;

import com.example.common.NotFoundException;
import java.util.HashMap;
import java.util.Map;

import com.example.pandemic.domain.exception.DomainException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

  // Obsługa ogólnych wyjątków
  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, String>> handleAllExceptions(Exception ex) {
    Map<String, String> error = new HashMap<>();
    error.put("error", "Internal server error");
    error.put("message", ex.getMessage());
    return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  // Obsługa wyjątków walidacji
  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult()
        .getFieldErrors()
        .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
    return errors;
  }

  @ExceptionHandler(DomainException.class)
  public ResponseEntity<Map<String, String>> handleDomainExceptions(Exception ex) {
    Map<String, String> error = new HashMap<>();
    error.put("error",  ex.getClass().getSimpleName());
    error.put("message", ex.getMessage());
    return new ResponseEntity<>(error, HttpStatus.NOT_ACCEPTABLE);
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<Map<String, String>> handleNotFound(NotFoundException ex) {
    Map<String, String> error = new HashMap<>();
    error.put("error", "Resource not found");
    error.put("message", ex.getMessage());
    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }
}
