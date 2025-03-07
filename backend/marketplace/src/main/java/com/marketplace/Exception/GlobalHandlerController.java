package com.marketplace.Exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalHandlerController {
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationError(MethodArgumentNotValidException ex) {
        Map<String, Object> body = new HashMap<String, Object>();
        body.put("Message", ex.getMessage());
        body.put("cause", ex.getCause());
        return new ResponseEntity<Object>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceFoundException.class)
    public ResponseEntity<Object> handleTheSameNameError(ResourceFoundException ex) {
        Map<String, Object> body = new HashMap<String, Object>();
        body.put("cause", ex.getCause());
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
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
