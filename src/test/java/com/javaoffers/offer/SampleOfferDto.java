package com.javaoffers.offer;

import com.javaoffers.offer.domain.dto.OfferDto;

public interface SampleOfferDto extends SampleOffer {

    default OfferDto offerDto1() {
        return OfferDto.builder()
                .id("1")
                .title("Junior Java Developer")
                .company("ABC")
                .salary("6k - 8k PLN")
                .offerUrl("https://example.com/offer1")
                .build();
    }

    default OfferDto offerDto2() {
        return OfferDto.builder()
                .id("2")
                .title("Junior Android Developer")
                .company("XYZ")
                .salary("7k - 10k PLN")
                .offerUrl("https://example.com/offer2")
                .build();
    }

    default OfferDto newOfferDto() {
        return OfferDto.builder()
                .id("newid")
                .title("title")
                .company("company")
                .salary("salary")
                .offerUrl("unique-url")
                .build();
    }

    default OfferDto newOfferDtoWithoutId() {
        return OfferDto.builder()
                .title("title")
                .company("company")
                .salary("salary")
                .offerUrl("unique-url")
                .build();
    }
}