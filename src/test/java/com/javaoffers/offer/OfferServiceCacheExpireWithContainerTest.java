package com.javaoffers.offer;

import com.javaoffers.JobOffersApplication;
import com.javaoffers.offer.domain.OfferRepository;
import com.javaoffers.offer.domain.OfferService;
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

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = OfferServiceCacheExpireWithContainerTest.Config.class)
@Testcontainers
@ActiveProfiles("redis")
public class OfferServiceCacheExpireWithContainerTest {

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
    void cache_should_expire_after_given_time() {
        // given
        Duration duration = Duration.ofSeconds(2);
        service.getAllOffers();
        assertThat(cacheManager.getCacheNames()).hasSize(1);

        // when
        // then
        await()
                .atMost(duration)
                .untilAsserted(() -> {
                    service.getAllOffers();
                    verify(repository, times(2)).findAll();
                });
    }

    @Import(JobOffersApplication.class)
    static class Config {

    }
}