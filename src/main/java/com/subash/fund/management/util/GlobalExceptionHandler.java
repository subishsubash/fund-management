package com.subash.fund.management.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

import static com.subash.fund.management.util.Constants.*;

/**
 * Global exception handler to manage all controller-level exceptions in a centralized way.
 * Logs the errors and returns appropriate HTTP responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles validation errors from @Valid annotated request bodies.
     *
     * @param ex {@link MethodArgumentNotValidException}
     * @return 400 BAD_REQUEST with field-specific validation error messages
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        logger.error(BAD_REQUEST + errors);
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles malformed JSON request bodies.
     *
     * @param ex {@link HttpMessageNotReadableException}
     * @return 400 BAD_REQUEST with message about unreadable JSON
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAll(Exception ex) {
        logger.error(UNEXPECTED_ERROR + ex.getMessage());
        return new ResponseEntity<>(UNEXPECTED_ERROR + API_PROCESSED_FAILURE, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Catch-all exception handler for unexpected errors.
     *
     * @param ex {@link Exception}
     * @return 500 INTERNAL_SERVER_ERROR with a generic failure message
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleMalformedJson(HttpMessageNotReadableException ex) {
        logger.error(MALFORMED_JSON + ex.getMessage());
        return new ResponseEntity<>(MALFORMED_JSON + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
