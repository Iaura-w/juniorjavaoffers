package com.javaoffers.exceptions;

import lombok.Value;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@Value
public class ValidationErrorResponse {
    ZonedDateTime timestamp;
    HttpStatus httpStatus;
    String message;
}