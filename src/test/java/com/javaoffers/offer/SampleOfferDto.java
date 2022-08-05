package com.javaoffers.offer;

import com.javaoffers.offer.domain.OfferMapper;
import com.javaoffers.offer.domain.dto.OfferDto;

public interface SampleOfferDto extends SampleOffer {

    default OfferDto offerDto1() {
        return OfferMapper.mapFromOfferToOfferDto(offer1());
    }

    default OfferDto offerDto2() {
        return OfferMapper.mapFromOfferToOfferDto(offer2());
    }
}