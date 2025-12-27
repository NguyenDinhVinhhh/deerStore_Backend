package com.example.quanlycuahang.handler;

import com.example.quanlycuahang.exception.BusinessException;
import com.example.quanlycuahang.exception.InventoryAdjustmentException;
import com.example.quanlycuahang.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Tạo một cấu trúc response chung cho lỗi
    private Map<String, Object> createErrorResponse(HttpStatus status, String message, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        body.put("path", request.getDescription(false).replace("uri=", ""));
        return body;
    }

    /**
     * Xử lý lỗi khi không tìm thấy tài nguyên (ví dụ: SP, Kho không tồn tại)
     * Ánh xạ sang HTTP 404 NOT FOUND.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {

        HttpStatus status = HttpStatus.NOT_FOUND;
        Map<String, Object> body = createErrorResponse(status, ex.getMessage(), request);
        return new ResponseEntity<>(body, status);
    }

    /**
     * Xử lý lỗi nghiệp vụ liên quan đến điều chỉnh tồn kho (ví dụ: Tồn kho âm)
     * Ánh xạ sang HTTP 400 BAD REQUEST.
     */
    @ExceptionHandler(InventoryAdjustmentException.class)
    public ResponseEntity<Object> handleInventoryAdjustmentException(
            InventoryAdjustmentException ex, WebRequest request) {

        HttpStatus status = HttpStatus.BAD_REQUEST;
        Map<String, Object> body = createErrorResponse(status, ex.getMessage(), request);
        return new ResponseEntity<>(body, status);
    }

    /**
     * Xử lý lỗi Bad Request chung (ví dụ: lỗi Validation từ @Valid)
     * (Thường phức tạp hơn, nhưng đây là ví dụ cơ bản)
     */
    // @ExceptionHandler(MethodArgumentNotValidException.class)
    // ...

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Object> handleBusinessException(
            BusinessException ex, WebRequest request) {

        HttpStatus status = HttpStatus.BAD_REQUEST; // hoặc CONFLICT
        Map<String, Object> body = createErrorResponse(
                status,
                ex.getMessage(),
                request
        );
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Object> handleIllegalState(
            IllegalStateException ex, WebRequest request) {

        HttpStatus status = HttpStatus.BAD_REQUEST;
        Map<String, Object> body = createErrorResponse(
                status,
                ex.getMessage(),
                request
        );
        return new ResponseEntity<>(body, status);
    }


}