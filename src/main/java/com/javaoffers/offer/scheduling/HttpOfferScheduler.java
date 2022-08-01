package com.javaoffers.offer.scheduling;

import com.javaoffers.infrastructure.RemoteOfferClient;
import com.javaoffers.infrastructure.offer.dto.HttpOfferDto;
import com.javaoffers.offer.domain.Offer;
import com.javaoffers.offer.domain.OfferService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class HttpOfferScheduler {

    private final RemoteOfferClient offerClient;
    private final OfferService service;

    @Scheduled(fixedDelayString = "${httpOfferScheduler.delay}")
    public void getOffers() {
        List<HttpOfferDto> offers = offerClient.getOffers();
        List<Offer> savedOffers = service.saveAllHttpOffers(offers);
        log.info("Added {} offers to database", savedOffers.size());
    }
}