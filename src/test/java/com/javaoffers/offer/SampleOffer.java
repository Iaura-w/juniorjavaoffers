package com.javaoffers.offer;

import com.javaoffers.offer.domain.Offer;

public interface SampleOffer {

    default Offer offer1() {
        return new Offer("1", "ABC", "Junior Java Developer", "6k - 8k PLN", "https://example.com/offer1");
    }

    default Offer offer2() {
        return new Offer("2", "XYZ", "Junior Android Developer", "7k - 10k PLN", "https://example.com/offer2");
    }
}