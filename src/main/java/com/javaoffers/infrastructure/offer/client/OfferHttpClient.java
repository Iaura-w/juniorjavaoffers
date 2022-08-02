package com.javaoffers.infrastructure.offer.client;

import com.javaoffers.infrastructure.RemoteOfferClient;
import com.javaoffers.infrastructure.offer.dto.HttpOfferDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Slf4j
public class OfferHttpClient implements RemoteOfferClient {

    private final RestTemplate restTemplate;
    private final String uri;

    @Override
    public List<HttpOfferDto> getOffers() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        final HttpEntity<HttpHeaders> requestEntity = new HttpEntity<>(httpHeaders);

        try {
            ResponseEntity<List<HttpOfferDto>> response = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, new ParameterizedTypeReference<List<HttpOfferDto>>() {
            });
            List<HttpOfferDto> offerList = response.getBody();
            return Optional.ofNullable(offerList).orElse(Collections.emptyList());
        } catch (RestClientException e) {
            log.error(e.getMessage());
            return Collections.emptyList();
        }
    }
}
