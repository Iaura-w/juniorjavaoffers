package com.javaoffers.offer.domain.exceptions;

public class OfferNotFoundException extends RuntimeException {

    public static final String MESSAGE = "Offer with id %s was not found";

    public OfferNotFoundException(String offerId) {
        super(String.format(MESSAGE, offerId));
    }
}