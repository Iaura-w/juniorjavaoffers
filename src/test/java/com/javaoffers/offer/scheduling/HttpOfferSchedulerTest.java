package com.javaoffers.offer.scheduling;

import com.javaoffers.JobOffersApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = HttpOfferSchedulerTest.Config.class)
@Testcontainers
@ActiveProfiles("scheduler")
public class HttpOfferSchedulerTest {

    @Container
    private static final MongoDBContainer DB_CONTAINER = new MongoDBContainer(DockerImageName.parse("mongo:5.0.9"));

    @SpyBean
    private HttpOfferScheduler httpOfferScheduler;

    static {
        DB_CONTAINER.start();
        String port = String.valueOf(DB_CONTAINER.getFirstMappedPort());
        System.setProperty("DB_PORT", port);
    }

    @Test
    void should_call_scheduled_two_times_when_wait_ten_seconds() {
        await()
                .atMost(Duration.ofSeconds(10))
                .untilAsserted(() -> verify(httpOfferScheduler, times(2)).getOffers());
    }

    @Import(JobOffersApplication.class)
    static class Config {

    }
}