package com.javaoffers.offer.domain;

import com.javaoffers.offer.SampleOfferDto;
import com.javaoffers.offer.domain.dto.OfferDto;
import com.javaoffers.offer.domain.exceptions.OfferNotFoundException;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OfferServiceTest implements SampleOfferDto {

    private final OfferService offerService = new OfferService();

    @Test
    void should_return_list_of_all_two_offers() {
        // given
        List<OfferDto> expected = Arrays.asList(offerDto1(), offerDto2());

        // when
        List<OfferDto> actual = offerService.getAllOffers();

        // then
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    void should_return_one_offer_by_id_if_found() {
        // given
        OfferDto expected = offerDto1();

        // when
        OfferDto actual = offerService.getOfferById(1);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void should_throw_when_offer_by_id_not_found() {
        // given
        long id = 341L;

        // when
        // then
        assertThatThrownBy(() -> offerService.getOfferById(id))
                .isInstanceOf(OfferNotFoundException.class)
                .hasMessageContaining(String.format("Offer with id %d was not found", id));
    }
}