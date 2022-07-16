package com.javaoffers.config;

import com.javaoffers.infrastructure.RemoteOfferClient;
import com.javaoffers.infrastructure.offer.client.OfferHttpClient;
import com.javaoffers.infrastructure.offer.error.RestTemplateResponseErrorHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class Config {

    @Bean
    RemoteOfferClient offerClient(RestTemplate restTemplate, @Value("${offer.http.client.config.uri:https://example.com}") String uri) {
        return new OfferHttpClient(restTemplate, uri);
    }

    @Bean
    RestTemplate restTemplate(RestTemplateResponseErrorHandler errorHandler,
                              @Value("${offer.http.client.config.connectionTimeout}") long connectionTimeout,
                              @Value("${offer.http.client.config.readTimeout}") long readTimeout) {
        return new RestTemplateBuilder()
                .errorHandler(errorHandler)
                .setConnectTimeout(Duration.ofMillis(connectionTimeout))
                .setReadTimeout(Duration.ofMillis(readTimeout))
                .build();
    }

    @Bean
    RestTemplateResponseErrorHandler errorHandler() {
        return new RestTemplateResponseErrorHandler();
    }
}
