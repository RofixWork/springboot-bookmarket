package com.book.library.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

public class NotFoundException extends ApiException{
    String message;
    HttpStatus status;

    public NotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
