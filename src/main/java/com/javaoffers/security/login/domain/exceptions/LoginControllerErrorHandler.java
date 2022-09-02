package com.javaoffers.security.login.domain.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.ZonedDateTime;

@RestControllerAdvice
@Slf4j
public class LoginControllerErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public LoginErrorResponse handleBadCredentialsException(BadCredentialsException e) {
        String message = e.getMessage();
        log.error(message);
        return new LoginErrorResponse(ZonedDateTime.now(), HttpStatus.UNAUTHORIZED, message);
    }
}