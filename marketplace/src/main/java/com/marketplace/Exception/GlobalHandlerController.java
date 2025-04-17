package com.marketplace.Exception;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.tomcat.util.http.fileupload.impl.IOFileUploadException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalHandlerController {
    
    private Map<String, List<String>> getErrorsMap(List<String> errors) {
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);
        return errorResponse;
    }

    // private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
    //     return new ResponseEntity<Object>(apiError, apiError.getStatus());
    // }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneralExceptionError(Exception ex) {
        log.error("General error exception", ex.getMessage());
        List<String> errors = Collections.singletonList(ex.getMessage());
        log.error("Error in Exception class, cause is: {}", ex.getMessage());  
        return new ResponseEntity<>(getErrorsMap(errors), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        List<String> errors = Collections.singletonList(ex.getMessage());
        log.error("Error in HttpMessageNotReadableException, cause is: {}", ex.getMostSpecificCause());
//        return new ResponseEntity<>(getErrorsMap(errors), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>("Please do not enter the empty file", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeExceptionError(RuntimeException ex) {
        log.error("Error in Runtime Exception class, cause is: {}", ex.getMessage());  
        List<String> errors = Collections.singletonList(ex.getMessage());
        return new ResponseEntity<>(getErrorsMap(errors), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Object> handleJDBCExceptionError(DataAccessException ex) {
        log.error("Error data access exception", ex.getMessage());
        log.error("Error data access exception cause", ex.getCause());
        // List<String> errors = Collections.singletonList(ex.getMessage());
        return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationError(MethodArgumentNotValidException ex) {
        log.error("method argument not valid exception", ex.getMessage());
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
            .map(FieldError::getDefaultMessage)
            .collect(Collectors.toList());
        return new ResponseEntity<Object>(getErrorsMap(errors), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IOFileUploadException.class)
    public ResponseEntity<Object> handleIOFileUploadException(IOFileUploadException ex) {
        log.error("file upload exception: {}", ex.getCause());
        return new ResponseEntity<>("Don't upload the file in this place", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Object> handleNoResourceFound(NoResourceFoundException ex) {
        log.error("No resource found: {}", ex.getCause());
        return new ResponseEntity<>("Resource Not found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<String> handleMaxSizeException(MaxUploadSizeExceededException exc) {
        log.error("Max size exception: {}" + exc.getMessage());
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("File too large!");
    }

    @ExceptionHandler(ResourceDuplicationException.class)
    public ResponseEntity<Object> handleTheSameNameError(ResourceDuplicationException ex) {
        log.error("Resource duplication exception", ex.getMessage());
        Map<String, Object> errorBody = new HashMap<String, Object>();
        errorBody.put("message", ex.getMessage());
        errorBody.put("cause", ex.getCause() != null ? ex.getCause().getMessage() : "Duplicate resource");
        return new ResponseEntity<>(errorBody, HttpStatus.FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalExceptionError(IllegalArgumentException ex) {
        log.error("Illegal argument exception error", ex.getMessage());
        Map<String, Object> errorBody = new HashMap<String, Object>();
        errorBody.put("message", ex.getMessage());
        errorBody.put("cause", ex.getCause() != null ? ex.getCause().getMessage() : "please make the good argument");
        return new ResponseEntity<>(errorBody, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleNotFoundError(ResourceNotFoundException ex) {
        log.error("Resource not found exception", ex.getMessage());
        Map<String, Object> body = new HashMap<String, Object>();
        body.put("cause", ex.getCause());
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.FOUND);
    }

}
