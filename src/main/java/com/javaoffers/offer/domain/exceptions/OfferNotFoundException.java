package com.javaoffers.offer.domain.exceptions;

public class OfferNotFoundException extends RuntimeException {
    public OfferNotFoundException(long id) {
        super(String.format("Offer with id %d was not found", id));
    }
}