package com.example.quanlycuahang.handler;

import com.example.quanlycuahang.exception.BusinessException;
import com.example.quanlycuahang.exception.DuplicateResourceException;
import com.example.quanlycuahang.exception.InventoryAdjustmentException;
import com.example.quanlycuahang.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

   // tạo mẫu lỗi chung
    private ErrorResponse buildErrorResponse(HttpStatus status, String message, WebRequest request) {
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(request.getDescription(false).replace("uri=", ""))
                .build();
    }

    // lỗi  valid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex, WebRequest request) {

        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));

        ErrorResponse error = buildErrorResponse(HttpStatus.BAD_REQUEST, errorMessage, request);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // lỗi không tìm thấy dữ liẹu
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {

        ErrorResponse error = buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // lỗi nghiệp vụ
    @ExceptionHandler({
            BusinessException.class,
            InventoryAdjustmentException.class,
            IllegalStateException.class
    })
    public ResponseEntity<ErrorResponse> handleBusinessExceptions(
            Exception ex, WebRequest request) {

        ErrorResponse error = buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // lỗi hệ thống
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, WebRequest request) {
        // In lỗi ra màn hình đen (Console) của IntelliJ để bạn đọc
        ex.printStackTrace();

        ErrorResponse error = buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Lỗi hệ thống: " + ex.getMessage(), // Hiển thị thông báo lỗi gốc
                request
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // lỗi trùng
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateResourceException(
            DuplicateResourceException ex, WebRequest request) {

        ErrorResponse error = buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage(), request);
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }
}