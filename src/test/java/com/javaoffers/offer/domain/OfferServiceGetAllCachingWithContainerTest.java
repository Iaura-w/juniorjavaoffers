package com.javaoffers.offer.domain;

import com.javaoffers.JobOffersApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = OfferServiceGetAllCachingWithContainerTest.Config.class)
@Testcontainers
@ActiveProfiles("redis")
public class OfferServiceGetAllCachingWithContainerTest {

    @Container
    private static final GenericContainer<?> REDIS = new GenericContainer<>(DockerImageName.parse("redis:7.0.4")).withExposedPorts(6379);

    @Container
    private static final MongoDBContainer DB_CONTAINER = new MongoDBContainer(DockerImageName.parse("mongo:5.0.9"));

    @Autowired
    private OfferService service;

    @MockBean
    private OfferRepository repository;

    @Autowired
    CacheManager cacheManager;

    static {
        DB_CONTAINER.start();
        System.setProperty("DB_PORT", String.valueOf(DB_CONTAINER.getFirstMappedPort()));
        REDIS.start();
        System.setProperty("redis.config.hostName", REDIS.getHost());
        System.setProperty("redis.config.port", REDIS.getFirstMappedPort().toString());
    }

    @Test
    void should_get_cached_offers_when_getAllOffers_called_twice() {
        // given
        assertThat(cacheManager.getCacheNames()).isEmpty();

        // when
        service.getAllOffers();
        service.getAllOffers();

        // then
        assertThat(cacheManager.getCacheNames()).hasSize(1);
        verify(repository, times(1)).findAll();
    }

    @Import(JobOffersApplication.class)
    static class Config {

    }
}