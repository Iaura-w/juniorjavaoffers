package com.javaoffers.offer;

import com.javaoffers.offer.domain.OfferMapper;
import com.javaoffers.offer.domain.dto.OfferDto;

public interface SampleOfferDto {

    default OfferDto offerDto1() {
        return OfferMapper.mapToOfferDto("Junior Java Developer", "ABC", "6k - 8k PLN", "https://example.com");
    }

    default OfferDto offerDto2() {
        return OfferMapper.mapToOfferDto("Junior Android Developer", "XYZ", "7k - 10k PLN", "https://example.com");
    }
}