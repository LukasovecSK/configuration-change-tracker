package com.example.configuration_change_tracker.exception;

import com.example.configuration_change_tracker.dto.ErrorResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

@RestControllerAdvice
public class RestExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

  @ExceptionHandler(EntityNotFoundException.class)
  protected ResponseEntity<Object> handleEntityNotFoundException(final EntityNotFoundException ex) {
    final ErrorResponseDTO error = ErrorResponseDTO.builder().message("Entity not found").details(List.of(ex.getMessage())).build();

    logger.error(error.getMessage(), ex);

    return ResponseEntity.status(404).contentType(MediaType.APPLICATION_JSON).body(error);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  protected ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
    var errors = ex.getBindingResult().getFieldErrors().stream().map(f -> f.getField() + ": " + f.getDefaultMessage()).toList();
    final ErrorResponseDTO error = ErrorResponseDTO.builder().message("Validation failed").details(errors).build();

    logger.error(error.getMessage(), ex);

    return ResponseEntity.status(400).contentType(MediaType.APPLICATION_JSON).body(error);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  protected ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex) {
    final List<String> errors = ex.getConstraintViolations().stream().map(cv -> cv.getPropertyPath() + ": " + cv.getMessage()).toList();
    final ErrorResponseDTO error = ErrorResponseDTO.builder().message("Validation failed").details(errors).build();

    logger.error(error.getMessage(), ex);

    return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(error);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  protected ResponseEntity<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
    final ErrorResponseDTO error = ErrorResponseDTO.builder().message("Invalid arguments").details(List.of(ex.getMessage())).build();

    logger.error(error.getMessage(), ex);

    return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(error);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  protected ResponseEntity<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
    final ErrorResponseDTO error = ErrorResponseDTO.builder().message("Invalid request body").details(List.of(ex.getMessage())).build();

    logger.error(error.getMessage(), ex);

    return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(error);
  }

  @ExceptionHandler(Exception.class)
  protected ResponseEntity<Object> handleException(final Exception ex) {
    final ErrorResponseDTO error = ErrorResponseDTO.builder().message("Internal server error").details(List.of(ex.getMessage())).build();

    logger.error(error.getMessage(), ex);

    return ResponseEntity.status(500).contentType(MediaType.APPLICATION_JSON).body(error);
  }
}
