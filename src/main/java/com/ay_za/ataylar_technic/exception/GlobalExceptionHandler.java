package com.ay_za.ataylar_technic.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleCategoryNotFound(CategoryNotFoundException ex) {
        return createErrorResponse(ex.getMessage(), ex.getErrorCode(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CategoryAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleCategoryAlreadyExists(CategoryAlreadyExistsException ex) {
        return createErrorResponse(ex.getMessage(), ex.getErrorCode(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CategoryDeletionException.class)
    public ResponseEntity<Map<String, Object>> handleCategoryDeletion(CategoryDeletionException ex) {
        return createErrorResponse(ex.getMessage(), ex.getErrorCode(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CategoryValidationException.class)
    public ResponseEntity<Map<String, Object>> handleCategoryValidation(CategoryValidationException ex) {
        return createErrorResponse(ex.getMessage(), ex.getErrorCode(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(QrCodeGenerationException.class)
    public ResponseEntity<Map<String, Object>> handleQrCodeGeneration(QrCodeGenerationException ex) {
        return createErrorResponse(ex.getMessage(), ex.getErrorCode(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InventoryCategoryException.class)
    public ResponseEntity<Map<String, Object>> handleInventoryCategoryException(InventoryCategoryException ex) {
        return createErrorResponse(ex.getMessage(), ex.getErrorCode(), HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Map<String, Object>> createErrorResponse(String message, String errorCode, HttpStatus status) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("success", false);
        errorResponse.put("message", message);
        errorResponse.put("errorCode", errorCode);
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("status", status.value());

        return ResponseEntity.status(status).body(errorResponse);
    }
}
