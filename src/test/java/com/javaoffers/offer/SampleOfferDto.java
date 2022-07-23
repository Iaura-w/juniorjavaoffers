package com.javaoffers.offer;

import com.javaoffers.offer.domain.OfferMapper;
import com.javaoffers.offer.domain.dto.OfferDto;

public interface SampleOfferDto extends SampleOffer {

    default OfferDto offerDto1() {
        return OfferMapper.mapToOfferDto(offer1());
    }

    default OfferDto offerDto2() {
        return OfferMapper.mapToOfferDto(offer2());
    }
}