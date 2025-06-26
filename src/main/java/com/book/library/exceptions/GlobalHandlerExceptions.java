package com.book.library.exceptions;

import com.book.library.payloads.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalHandlerExceptions {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex)
    {
        var response = new HashMap<String, String>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError)(error)).getField(),
                    fieldValue = error.getDefaultMessage();

            response.put(fieldName, fieldValue);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse> handleNotFoundException(NotFoundException ex)
    {
        String message = ex.getMessage();
        ApiResponse response = new ApiResponse(message, false);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiResponse> handleApiException(ConflictException ex)
    {
        String message = ex.getMessage();
        ApiResponse response = new ApiResponse(message, false);
        return ResponseEntity.status(ex.getStatus()).body(response);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse> handleBadRequestException(BadRequestException ex)
    {
        String message = ex.getMessage();
        ApiResponse response = new ApiResponse(message, false);
        return ResponseEntity.status(ex.getStatus()).body(response);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse> handleApiException(ApiException ex)
    {
        String message = ex.getMessage();
        ApiResponse response = new ApiResponse(message, false);
        return ResponseEntity.status(ex.getStatus()).body(response);
    }
}
