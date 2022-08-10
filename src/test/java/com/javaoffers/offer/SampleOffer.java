package com.javaoffers.offer;

import com.javaoffers.offer.domain.Offer;

public interface SampleOffer {

    default Offer offer1() {
        return Offer.builder()
                .id("1")
                .title("Junior Java Developer")
                .company("ABC")
                .salary("6k - 8k PLN")
                .offerUrl("https://example.com/offer1")
                .build();
    }

    default Offer offer2() {
        return Offer.builder()
                .id("2")
                .title("Junior Android Developer")
                .company("XYZ")
                .salary("7k - 10k PLN")
                .offerUrl("https://example.com/offer2")
                .build();
    }

    default Offer newOffer() {
        return Offer.builder()
                .id("newid")
                .title("title")
                .company("company")
                .salary("salary")
                .offerUrl("unique-url")
                .build();
    }

    default Offer newOfferWithoutId() {
        return Offer.builder()
                .title("title")
                .company("company")
                .salary("salary")
                .offerUrl("unique-url")
                .build();
    }
}