package com.book.library.exceptions;

import org.springframework.http.HttpStatus;

public class BadRequestException extends ApiException{
    String message;
    HttpStatus status;

    public BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
