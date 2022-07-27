package com.javaoffers.offer.domain.exceptions;

public class OfferNotFoundException extends RuntimeException {

    private static final String MESSAGE = "Offer with id %s was not found";

    public OfferNotFoundException(String offerId) {
        super(getMessage(offerId));
    }

    private static String getMessage(String offerId) {
        return String.format(MESSAGE, offerId);
    }
}