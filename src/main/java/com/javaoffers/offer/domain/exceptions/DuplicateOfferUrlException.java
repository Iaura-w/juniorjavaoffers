package com.javaoffers.offer.domain.exceptions;

public class DuplicateOfferUrlException extends RuntimeException {

    public static final String MESSAGE = "Offer with url '%s' already exists";

    public DuplicateOfferUrlException(String offerUrl) {
        super(getMessage(offerUrl));
    }

    private static String getMessage(String offerUrl) {
        return String.format(MESSAGE, offerUrl);
    }
}