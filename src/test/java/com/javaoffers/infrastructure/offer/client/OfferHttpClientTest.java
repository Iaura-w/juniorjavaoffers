package com.javaoffers.infrastructure.offer.client;

import com.javaoffers.infrastructure.offer.dto.OfferDto;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OfferHttpClientTest {


    @Test
    void should_return_list_of_one_offer() {
        // given
        RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
        String uri = "test";

        when(restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<ParameterizedTypeReference<List<OfferDto>>>any()
        )).thenReturn(new ResponseEntity<>(Collections.singletonList(new OfferDto()), HttpStatus.ACCEPTED));

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

        when(restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<ParameterizedTypeReference<List<OfferDto>>>any()
        )).thenReturn(new ResponseEntity<>(Collections.emptyList(), HttpStatus.ACCEPTED));

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

        OfferDto offerDto = new OfferDto("title","example","test","test");
        OfferDto offerDto2 = new OfferDto("title2","example2","test","test");

        when(restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<ParameterizedTypeReference<List<OfferDto>>>any()
        )).thenReturn(new ResponseEntity<>(Arrays.asList(offerDto, offerDto2), HttpStatus.ACCEPTED));

        OfferHttpClient offerHttpClient = new OfferHttpClient(restTemplate, uri);

        // when
        List<OfferDto> offers = offerHttpClient.getOffers();

        // then
        assertThat(offers.size()).isEqualTo(2);
    }

}