package com.javaoffers.infrastructure;

import com.javaoffers.infrastructure.offer.dto.OfferDto;

import java.util.List;

public interface RemoteOfferClient {
    List<OfferDto> getOffers();
}
