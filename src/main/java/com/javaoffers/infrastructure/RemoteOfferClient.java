package com.javaoffers.infrastructure;

import com.javaoffers.infrastructure.offer.dto.HttpOfferDto;

import java.util.List;

public interface RemoteOfferClient {
    List<HttpOfferDto> getOffers();
}
