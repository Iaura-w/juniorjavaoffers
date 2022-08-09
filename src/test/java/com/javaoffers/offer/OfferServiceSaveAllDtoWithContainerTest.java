package com.javaoffers.offer;

import com.javaoffers.JobOffersApplication;
import com.javaoffers.infrastructure.offer.dto.HttpOfferDto;
import com.javaoffers.offer.domain.Offer;
import com.javaoffers.offer.domain.OfferMapper;
import com.javaoffers.offer.domain.OfferRepository;
import com.javaoffers.offer.domain.OfferService;
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
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = OfferServiceSaveAllDtoWithContainerTest.Config.class)
@Testcontainers
@ActiveProfiles("container")
public class OfferServiceSaveAllDtoWithContainerTest {

    @Container
    private static final MongoDBContainer DB_CONTAINER = new MongoDBContainer(DockerImageName.parse("mongo:5.0.9"));

    static {
        DB_CONTAINER.start();
        String port = DB_CONTAINER.getFirstMappedPort().toString();
        System.setProperty("DB_PORT", port);
    }

    @Autowired
    private OfferService service;

    @Autowired
    private OfferRepository repository;

    @Test
    void should_add_offers_from_http_client_when_not_already_exist() {
        // given
        HttpOfferDto newOfferDto1 = new HttpOfferDto("new title1", "new company1", "6k - 8k PLN", "https://example.com/newoffer1");
        HttpOfferDto newOfferDto2 = new HttpOfferDto("new title2", "new company2", "6k - 8k PLN", "https://example.com/newoffer2");
        List<HttpOfferDto> offerDtoList = Arrays.asList(newOfferDto1, newOfferDto2);
        List<Offer> offerList = offerDtoList.stream().map(OfferMapper::mapFromHttpOfferDtoToOffer).collect(Collectors.toList());

        assertThat(repository.findAll()).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id").doesNotContainAnyElementsOf(offerList);

        // when
        List<Offer> actual = service.saveAllOffersDto(offerDtoList);

        // then
        assertThat(repository.findAll())
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .containsAll(offerList);
        assertThat(actual)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .containsExactlyInAnyOrderElementsOf(offerList);
    }

    @Test
    void should_not_add_offers_from_http_client_when_already_exists() {
        // given
        HttpOfferDto offerDto = new HttpOfferDto("title", "company", "6k - 8k PLN", "https://example.com/not_unique_offer");
        List<HttpOfferDto> offerDtoList = Arrays.asList(offerDto);

        Offer existingOffer = new Offer(null, "company", "title", "6k - 8k PLN", "https://example.com/not_unique_offer");
        repository.save(existingOffer);
        int expectedNumberOfOffers = repository.findAll().size();

        assertThat(repository.existsByOfferUrl("https://example.com/not_unique_offer")).isTrue();

        // when
        List<Offer> actual = service.saveAllOffersDto(offerDtoList);
        int actualNumberOfOffers = repository.findAll().size();

        // then
        assertThat(repository.existsByOfferUrl("https://example.com/not_unique_offer")).isTrue();
        assertThat(actualNumberOfOffers).isEqualTo(expectedNumberOfOffers);
        assertThat(actual.size()).isEqualTo(0);
    }

    @Import(JobOffersApplication.class)
    static class Config {

    }
}