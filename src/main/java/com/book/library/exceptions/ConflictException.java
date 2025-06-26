package com.book.library.exceptions;

import org.springframework.http.HttpStatus;

public class ConflictException extends ApiException{
    String message;
    HttpStatus status;

    public ConflictException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
