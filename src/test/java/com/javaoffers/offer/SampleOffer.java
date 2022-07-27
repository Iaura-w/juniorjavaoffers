package com.javaoffers.offer;

import com.javaoffers.offer.domain.Offer;

public interface SampleOffer {

    default Offer offer1() {
        return new Offer("1", "Junior Java Developer", "ABC", "6k - 8k PLN", "https://example.com");
    }

    default Offer offer2() {
        return new Offer("2", "Junior Android Developer", "XYZ", "7k - 10k PLN", "https://example.com");
    }
}