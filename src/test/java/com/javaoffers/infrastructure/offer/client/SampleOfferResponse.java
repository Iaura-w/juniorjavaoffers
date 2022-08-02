package com.javaoffers.infrastructure.offer.client;

import com.javaoffers.infrastructure.offer.dto.HttpOfferDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public interface SampleOfferResponse {

    default ResponseEntity<List<HttpOfferDto>> responseWithOneOffer() {
        return new ResponseEntity<>(Collections.singletonList(new HttpOfferDto()), HttpStatus.ACCEPTED);
    }

    default ResponseEntity<List<HttpOfferDto>> responseWithNoOffers() {
        return new ResponseEntity<>(Collections.emptyList(), HttpStatus.ACCEPTED);
    }

    default ResponseEntity<List<HttpOfferDto>> responseWithTwoOffers() {
        return new ResponseEntity<>(Arrays.asList(new HttpOfferDto(), new HttpOfferDto()), HttpStatus.ACCEPTED);
    }
}
