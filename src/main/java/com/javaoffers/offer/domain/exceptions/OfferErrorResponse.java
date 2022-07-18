package com.javaoffers.offer.domain.exceptions;

import lombok.Value;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@Value
public class OfferErrorResponse {
    ZonedDateTime timestamp;
    HttpStatus httpStatus;
    String message;
}