package com.javaoffers.offer.domain;

import com.javaoffers.JobOffersApplication;
import com.javaoffers.offer.SampleOfferDto;
import com.javaoffers.offer.domain.dto.OfferDto;
import com.javaoffers.offer.domain.exceptions.OfferNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.BDDAssertions.then;

@SpringBootTest(classes = JobOffersApplication.class)
@Testcontainers
@ActiveProfiles("container")
public class OfferServiceWithContainerTest implements SampleOfferDto {

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
    void should_return_list_of_all_two_offers() {
        // given
        List<OfferDto> expected = Arrays.asList(offerDto1(), offerDto2());
        List<Offer> offers = Arrays.asList(offer1(), offer2());
        then(repository.findAll()).containsAll(offers);

        // when
        List<OfferDto> actual = service.getAllOffers();

        // then
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    void should_return_one_offer_by_id_if_found() {
        // given
        OfferDto expected = offerDto2();
        String id = "2";
        then(repository.findById(id)).isPresent();

        // when
        OfferDto actual = service.getOfferById(id);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void should_throw_exception_when_offer_by_id_not_found() {
        // given
        String id = "522";
        then(repository.findById(id)).isNotPresent();

        // when
        // then
        assertThatThrownBy(() -> service.getOfferById(id))
                .isInstanceOf(OfferNotFoundException.class)
                .hasMessageContaining(String.format("Offer with id %s was not found", id));
    }
}