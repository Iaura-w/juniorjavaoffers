package com.javaoffers.offer.domain;

import com.javaoffers.infrastructure.offer.dto.HttpOfferDto;
import com.javaoffers.offer.domain.dto.OfferDto;
import com.javaoffers.offer.domain.exceptions.OfferNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OfferService {
    private final OfferRepository repository;

    public List<OfferDto> getAllOffers() {
        return repository.findAll()
                .stream()
                .map(OfferMapper::mapToOfferDto)
                .collect(Collectors.toList());
    }

    public OfferDto getOfferById(String id) {
        return repository.findById(id)
                .map(OfferMapper::mapToOfferDto)
                .orElseThrow(() -> new OfferNotFoundException(id));
    }

    public List<Offer> saveAll(List<Offer> offers) {
        return repository.saveAll(offers);
    }

    public List<Offer> saveAllHttpOffers(List<HttpOfferDto> offersDto) {
        List<Offer> offerList = offersDto.stream()
                .filter(offerDto -> offerDto.getOfferUrl() != null)
                .filter(offerDto -> !offerDto.getOfferUrl().isEmpty())
                .filter(offerDto -> !repository.existsByOfferUrl(offerDto.getOfferUrl()))
                .map(OfferMapper::mapToOffer)
                .collect(Collectors.toList());

        return repository.saveAll(offerList);
    }
}