package com.javaoffers.offer.domain.exceptions;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class OfferControllerErrorHandlerTest {

    OfferControllerErrorHandler offerControllerErrorHandler = new OfferControllerErrorHandler();

    @Test
    void should_return_correct_response() {
        // given
        long id = 231L;
        OfferErrorResponse expected = new OfferErrorResponse(ZonedDateTime.now(), HttpStatus.NOT_FOUND, String.format("Offer with id %d was not found", id));

        // when
        OfferErrorResponse actual = offerControllerErrorHandler.handleOfferNotFoundException(new OfferNotFoundException(id));

        // then
        assertThat(actual).usingRecursiveComparison().ignoringFields("timestamp").isEqualTo(expected);
    }
}