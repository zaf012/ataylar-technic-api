package com.ay_za.ataylar_technic.exception;

import com.ay_za.ataylar_technic.error.ErrorLoggingService;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private ErrorLoggingService errorLoggingService;

    // Mevcut Category Exception'ları
    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleCategoryNotFound(CategoryNotFoundException ex) {
        errorLoggingService.logError(ex, ex.getErrorCode(), "WARN");
        return createErrorResponse(ex.getMessage(), ex.getErrorCode(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CategoryAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleCategoryAlreadyExists(CategoryAlreadyExistsException ex) {
        errorLoggingService.logError(ex, ex.getErrorCode(), "WARN");
        return createErrorResponse(ex.getMessage(), ex.getErrorCode(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CategoryDeletionException.class)
    public ResponseEntity<Map<String, Object>> handleCategoryDeletion(CategoryDeletionException ex) {
        errorLoggingService.logError(ex, ex.getErrorCode(), "ERROR");
        return createErrorResponse(ex.getMessage(), ex.getErrorCode(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CategoryValidationException.class)
    public ResponseEntity<Map<String, Object>> handleCategoryValidation(CategoryValidationException ex) {
        errorLoggingService.logError(ex, ex.getErrorCode(), "WARN");
        return createErrorResponse(ex.getMessage(), ex.getErrorCode(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(QrCodeGenerationException.class)
    public ResponseEntity<Map<String, Object>> handleQrCodeGeneration(QrCodeGenerationException ex) {
        errorLoggingService.logError(ex, ex.getErrorCode(), "ERROR");
        return createErrorResponse(ex.getMessage(), ex.getErrorCode(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InventoryCategoryException.class)
    public ResponseEntity<Map<String, Object>> handleInventoryCategoryException(InventoryCategoryException ex) {
        errorLoggingService.logError(ex, ex.getErrorCode(), "ERROR");
        return createErrorResponse(ex.getMessage(), ex.getErrorCode(), HttpStatus.BAD_REQUEST);
    }

    // Yeni Business Exception'lar

    // Firm Exception'ları
    @ExceptionHandler(FirmNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleFirmNotFound(FirmNotFoundException ex) {
        errorLoggingService.logError(ex, ex.getErrorCode(), "WARN");
        return createErrorResponse(ex.getMessage(), ex.getErrorCode(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FirmAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleFirmAlreadyExists(FirmAlreadyExistsException ex) {
        errorLoggingService.logError(ex, ex.getErrorCode(), "WARN");
        return createErrorResponse(ex.getMessage(), ex.getErrorCode(), HttpStatus.CONFLICT);
    }

    // Genel Validation ve Data Exception'ları
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(ValidationException ex) {
        errorLoggingService.logError(ex, ex.getErrorCode(), "WARN");
        return createErrorResponse(ex.getMessage(), ex.getErrorCode(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleDataNotFound(DataNotFoundException ex) {
        errorLoggingService.logError(ex, ex.getErrorCode(), "WARN");
        return createErrorResponse(ex.getMessage(), ex.getErrorCode(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleDataAlreadyExists(DataAlreadyExistsException ex) {
        errorLoggingService.logError(ex, ex.getErrorCode(), "WARN");
        return createErrorResponse(ex.getMessage(), ex.getErrorCode(), HttpStatus.CONFLICT);
    }

    // Standard Java Exception'ları
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        errorLoggingService.logError(ex, "ILLEGAL_ARGUMENT", "ERROR");
        return createErrorResponse(ex.getMessage(), "ILLEGAL_ARGUMENT", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
        errorLoggingService.logError(ex, "RUNTIME_ERROR", "ERROR");
        return createErrorResponse("Sistem hatası: " + ex.getMessage(), "RUNTIME_ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Spring Validation Exception'ları
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        errorLoggingService.logError(ex, "VALIDATION_ERROR", "WARN");
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("success", false);
        errorResponse.put("message", "Validasyon hatası");
        errorResponse.put("errorCode", "VALIDATION_ERROR");
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
        errorResponse.put("fieldErrors", errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolation(ConstraintViolationException ex) {
        errorLoggingService.logError(ex, "CONSTRAINT_VIOLATION", "WARN");
        return createErrorResponse("Validasyon hatası: " + ex.getMessage(), "CONSTRAINT_VIOLATION", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        errorLoggingService.logError(ex, "TYPE_MISMATCH", "WARN");
        String message = String.format("'%s' parametresi için geçersiz değer: '%s'", ex.getName(), ex.getValue());
        return createErrorResponse(message, "TYPE_MISMATCH", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        errorLoggingService.logError(ex, "JSON_PARSE_ERROR", "WARN");
        return createErrorResponse("Geçersiz JSON formatı", "JSON_PARSE_ERROR", HttpStatus.BAD_REQUEST);
    }

    // NoResourceFoundException - Spring'in statik kaynak hatalarını yakala ama log'lama
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNoResourceFound(NoResourceFoundException ex) {
        // Bu hatayı log'lamıyoruz çünkü normal bir durum (API endpoint'i yok)
        return createErrorResponse("Endpoint bulunamadı: " + ex.getResourcePath(), "NOT_FOUND", HttpStatus.NOT_FOUND);
    }

    // HttpMediaTypeNotAcceptableException - Accept header hatası
    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<Map<String, Object>> handleMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex) {
        String message = "Geçersiz Accept header formatı. Lütfen 'Accept: application/json' veya 'Accept: */*' kullanın.";
        errorLoggingService.logError(ex, "INVALID_ACCEPT_HEADER", "WARN");
        return createErrorResponse(message, "INVALID_ACCEPT_HEADER", HttpStatus.NOT_ACCEPTABLE);
    }

    // Genel Exception Handler - En son yakalanır
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        errorLoggingService.logError(ex, "INTERNAL_ERROR", "CRITICAL");
        return createErrorResponse("Beklenmeyen bir hata oluştu", "INTERNAL_ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
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
