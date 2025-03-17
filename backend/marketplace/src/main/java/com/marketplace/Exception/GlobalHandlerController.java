package com.marketplace.Exception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalHandlerController {
    
    private Map<String, List<String>> getErrorsMap(List<String> errors) {
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);
        return errorResponse;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAnyExceptionError(Exception ex) {
        Map<String, Object> errorBody = new HashMap<String, Object>();
        errorBody.put("message", ex.getCause().getMessage());
        // errorBody.put("cause", ex.getCause());
        // List<String> errors = ex.getCause().getMessage()
        log.error("Something went wrong", ex.getCause().getMessage());  
        return new ResponseEntity<Object>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationError(MethodArgumentNotValidException ex) {
        // Map<String, Object> body = new HashMap<String, Object>();
        // body.put("Message", ex.getMessage());
        // body.put("cause", ex.getCause());
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
            .map(FieldError::getDefaultMessage)
            .collect(Collectors.toList());
        return new ResponseEntity<Object>(getErrorsMap(errors), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceFoundException.class)
    public ResponseEntity<Object> handleTheSameNameError(ResourceFoundException ex) {
        Map<String, Object> errorBody = new HashMap<String, Object>();
        errorBody.put("message", ex.getMessage());
        errorBody.put("cause", ex.getCause() != null ? ex.getCause().getMessage() : "Duplicate resource");
        return new ResponseEntity<>(errorBody, HttpStatus.FOUND);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleNotFoundError(ResourceNotFoundException ex) {
        Map<String, Object> body = new HashMap<String, Object>();
        body.put("cause", ex.getCause());
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleInvalidArgument(IllegalArgumentException ex) {
        Map<String, Object> body = new HashMap<String, Object>();
        body.put("cause", ex.getCause());
        body.put("message", ex.getMessage());
        return new ResponseEntity<Object>(body, HttpStatus.BAD_REQUEST);
    }

}
