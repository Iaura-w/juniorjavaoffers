package com.javaoffers.security.login.domain.exceptions;

import lombok.Value;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@Value
public class LoginErrorResponse {
    ZonedDateTime timestamp;
    HttpStatus httpStatus;
    String message;
}