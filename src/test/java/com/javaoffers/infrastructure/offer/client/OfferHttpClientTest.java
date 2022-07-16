package com.javaoffers.infrastructure.offer.client;

import com.javaoffers.infrastructure.offer.dto.OfferDto;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OfferHttpClientTest implements SampleExchangeResponse, SampleOfferResponse {

    @Test
    void should_return_list_of_one_offer() {
        // given
        RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
        String uri = "test";

        when(getExchange(restTemplate))
                .thenReturn(responseWithOneOffer());

        OfferHttpClient offerHttpClient = new OfferHttpClient(restTemplate, uri);

        // when
        List<OfferDto> offers = offerHttpClient.getOffers();

        // then
        assertThat(offers.size()).isEqualTo(1);
    }

    @Test
    void should_return_empty_list_of_offers() {
        // given
        RestTemplate restTemplate = mock(RestTemplate.class);
        String uri = "test";

        when(getExchange(restTemplate))
                .thenReturn(responseWithNoOffers());

        OfferHttpClient offerHttpClient = new OfferHttpClient(restTemplate, uri);

        // when
        List<OfferDto> offers = offerHttpClient.getOffers();

        // then
        assertThat(offers.size()).isEqualTo(0);
    }

    @Test
    void should_return_list_of_two_offers() {
        // given
        RestTemplate restTemplate = mock(RestTemplate.class);
        String uri = "test";

        when(getExchange(restTemplate))
                .thenReturn(responseWithTwoOffers());

        OfferHttpClient offerHttpClient = new OfferHttpClient(restTemplate, uri);

        // when
        List<OfferDto> offers = offerHttpClient.getOffers();

        // then
        assertThat(offers.size()).isEqualTo(2);
    }

}