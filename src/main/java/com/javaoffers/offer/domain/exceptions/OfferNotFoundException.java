package com.javaoffers.offer.domain.exceptions;

public class OfferNotFoundException extends RuntimeException {

    public static final String MESSAGE = "Offer with id %d was not found";

    public OfferNotFoundException(long offerId) {
        super(String.format(MESSAGE, offerId));
    }
}