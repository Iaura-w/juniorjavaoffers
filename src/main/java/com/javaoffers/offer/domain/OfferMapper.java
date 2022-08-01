package com.javaoffers.offer.domain;

import com.javaoffers.infrastructure.offer.dto.HttpOfferDto;
import com.javaoffers.offer.domain.dto.OfferDto;

public class OfferMapper {

    public static OfferDto mapToOfferDto(Offer offer) {
        return OfferDto.builder()
                .id(offer.getId())
                .title(offer.getTitle())
                .company(offer.getCompany())
                .salary(offer.getSalary())
                .offerUrl(offer.getOfferUrl())
                .build();
    }

    public static Offer mapToOffer(HttpOfferDto offerDto) {
        Offer offer = new Offer();
        offer.setCompany(offerDto.getCompany());
        offer.setTitle(offerDto.getTitle());
        offer.setSalary(offerDto.getSalary());
        offer.setOfferUrl(offerDto.getOfferUrl());
        return offer;
    }
}
