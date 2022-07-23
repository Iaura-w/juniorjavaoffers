package com.javaoffers.offer.domain;

import com.javaoffers.offer.SampleOffer;
import com.javaoffers.offer.SampleOfferDto;
import com.javaoffers.offer.domain.dto.OfferDto;
import com.javaoffers.offer.domain.exceptions.OfferNotFoundException;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OfferServiceTest implements SampleOfferDto, SampleOffer {

    private final OfferRepository repository = mock(OfferRepository.class);
    private final OfferService offerService = new OfferService(repository);

    @Test
    void should_return_list_of_all_two_offers() {
        // given
        when(repository.findAll()).thenReturn(Arrays.asList(offer1(), offer2()));
        List<OfferDto> expected = Arrays.asList(offerDto1(), offerDto2());

        // when
        List<OfferDto> actual = offerService.getAllOffers();

        // then
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    void should_return_one_offer_by_id_if_found() {
        // given
        String id = "1";
        when(repository.findById(id)).thenReturn(Optional.ofNullable(offer1()));
        OfferDto expected = offerDto1();

        // when
        OfferDto actual = offerService.getOfferById(id);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void should_throw_when_offer_by_id_not_found() {
        // given
        String id = "341";
        when(repository.findById(id)).thenReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> offerService.getOfferById(id))
                .isInstanceOf(OfferNotFoundException.class)
                .hasMessageContaining(String.format("Offer with id %s was not found", id));
    }
}