package com.javaoffers.offer.domain;

import com.javaoffers.infrastructure.offer.dto.HttpOfferDto;
import com.javaoffers.offer.domain.dto.OfferDto;

public class OfferMapper {

    public static OfferDto mapFromOfferToOfferDto(Offer offer) {
        return OfferDto.builder()
                .id(offer.getId())
                .title(offer.getTitle())
                .company(offer.getCompany())
                .salary(offer.getSalary())
                .offerUrl(offer.getOfferUrl())
                .build();
    }

    public static Offer mapFromHttpOfferDtoToOffer(HttpOfferDto offerDto) {
        return Offer.builder()
                .company(offerDto.getCompany())
                .title(offerDto.getTitle())
                .salary(offerDto.getSalary())
                .offerUrl(offerDto.getOfferUrl())
                .build();
    }

    public static Offer mapFromOfferDtoToOffer(OfferDto offerDto) {
        return Offer.builder()
                .company(offerDto.getCompany())
                .title(offerDto.getTitle())
                .salary(offerDto.getSalary())
                .offerUrl(offerDto.getOfferUrl())
                .build();
    }
}
