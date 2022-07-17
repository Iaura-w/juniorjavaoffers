package com.javaoffers.offer.domain;

import com.javaoffers.offer.domain.dto.OfferDto;

public class OfferMapper {

    public static OfferDto mapToOfferDto(String title, String company, String salary, String offerUrl) {
        return OfferDto.builder()
                .title(title)
                .company(company)
                .salary(salary)
                .offerUrl(offerUrl)
                .build();
    }
}
