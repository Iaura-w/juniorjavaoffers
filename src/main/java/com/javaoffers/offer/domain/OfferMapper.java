package com.javaoffers.offer.domain;

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
}
