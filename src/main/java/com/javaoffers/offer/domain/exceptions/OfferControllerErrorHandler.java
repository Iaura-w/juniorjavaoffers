package com.javaoffers.offer.domain.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.ZonedDateTime;

@RestControllerAdvice
@Slf4j
public class OfferControllerErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(OfferNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public OfferErrorResponse handleOfferNotFoundException(OfferNotFoundException e) {
        String message = e.getMessage();
        log.error(message);
        return new OfferErrorResponse(ZonedDateTime.now(), HttpStatus.NOT_FOUND, message);
    }
}