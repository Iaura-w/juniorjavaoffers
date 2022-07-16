package com.javaoffers.config;

import com.javaoffers.infrastructure.RemoteOfferClient;
import org.springframework.web.client.RestTemplate;

public class TestConfig extends Config {

    public RemoteOfferClient offerTestClient(String uri, int connectionTimeout, int readTimeout) {
        RestTemplate restTemplate = restTemplate(errorHandler(), connectionTimeout, readTimeout);
        return offerClient(restTemplate, uri);
    }
}
