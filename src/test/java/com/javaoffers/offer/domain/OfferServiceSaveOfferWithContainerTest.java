package com.javaoffers.offer.domain;

import com.javaoffers.JobOffersApplication;
import com.javaoffers.offer.SampleOfferDto;
import com.javaoffers.offer.domain.dto.OfferDto;
import com.javaoffers.offer.domain.exceptions.DuplicateOfferUrlException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(classes = OfferServiceSaveOfferWithContainerTest.Config.class)
@Testcontainers
@ActiveProfiles("container")
public class OfferServiceSaveOfferWithContainerTest implements SampleOfferDto {

    @Container
    private static final MongoDBContainer DB_CONTAINER = new MongoDBContainer(DockerImageName.parse("mongo:5.0.9"));

    @Autowired
    OfferRepository repository;

    @Autowired
    OfferService service;

    static {
        DB_CONTAINER.start();
        String port = DB_CONTAINER.getFirstMappedPort().toString();
        System.setProperty("DB_PORT", port);
    }

    @Test
    void should_add_new_offer_when_valid_and_offer_url_is_not_duplicate() {
        // given
        OfferDto offerDto = newOfferDto();
        assertThat(repository.existsByOfferUrl(offerDto.getOfferUrl())).isFalse();

        // when
        OfferDto actual = service.saveOffer(offerDto);

        // then
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(offerDto);
        assertThat(repository.existsByOfferUrl(actual.getOfferUrl())).isTrue();
    }

    @Test
    void should_throw_when_offer_url_is_duplicate() {
        // given
        Offer savedOffer = offer1();
        OfferDto duplicateOffer = offerDto1();
        repository.save(savedOffer);

        assertThat(repository.existsByOfferUrl(duplicateOffer.getOfferUrl())).isTrue();

        // when
        // then
        assertThatThrownBy(() -> service.saveOffer(duplicateOffer))
                .isInstanceOf(DuplicateOfferUrlException.class)
                .hasMessageContaining(String.format("Offer with url '%s' already exists", duplicateOffer.getOfferUrl()));
    }

    @Import(JobOffersApplication.class)
    static class Config {

    }
}