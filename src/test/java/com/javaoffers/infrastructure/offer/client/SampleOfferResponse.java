package com.javaoffers.infrastructure.offer.client;

import com.javaoffers.infrastructure.offer.dto.OfferDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public interface SampleOfferResponse {

    default ResponseEntity<List<OfferDto>> responseWithOneOffer() {
        return new ResponseEntity<>(Collections.singletonList(new OfferDto()), HttpStatus.ACCEPTED);
    }

    default ResponseEntity<List<OfferDto>> responseWithNoOffers() {
        return new ResponseEntity<>(Collections.emptyList(), HttpStatus.ACCEPTED);
    }

    default ResponseEntity<List<OfferDto>> responseWithTwoOffers() {
        return new ResponseEntity<>(Arrays.asList(new OfferDto(), new OfferDto()), HttpStatus.ACCEPTED);
    }
}
