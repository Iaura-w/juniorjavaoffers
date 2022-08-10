package com.javaoffers.offer.domain;

import com.javaoffers.JobOffersApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = OfferServiceSaveAllWithContainerTest.Config.class)
@Testcontainers
@ActiveProfiles("container")
public class OfferServiceSaveAllWithContainerTest {

    @Container
    private static final MongoDBContainer DB_CONTAINER = new MongoDBContainer(DockerImageName.parse("mongo:5.0.9"));

    @Autowired
    private OfferRepository repository;
    @Autowired
    private OfferService service;

    static {
        DB_CONTAINER.start();
        String port = String.valueOf(DB_CONTAINER.getFirstMappedPort());
        System.setProperty("DB_PORT", port);
    }

    @Test
    void should_correctly_add_all_offers_to_database() {
        // given
        Offer newOffer1 = new Offer(null, "new company1", "new title1", "6k - 8k PLN", "https://newexample1.com");
        Offer newOffer2 = new Offer(null, "new company2", "new title2", "6k - 8k PLN", "https://newexample2.com");
        List<Offer> offers = Arrays.asList(newOffer1, newOffer2);

        assertThat(repository.findAll()).doesNotContainAnyElementsOf(offers);

        // when
        List<Offer> actual = service.saveAll(offers);

        // then
        assertThat(repository.findAll()).containsAll(offers);
        assertThat(actual).containsExactlyInAnyOrderElementsOf(offers);
    }

    @Import(JobOffersApplication.class)
    static class Config {

    }
}