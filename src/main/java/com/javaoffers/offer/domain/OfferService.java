package com.javaoffers.offer.domain;

import com.javaoffers.infrastructure.offer.dto.HttpOfferDto;
import com.javaoffers.offer.domain.dto.OfferDto;
import com.javaoffers.offer.domain.exceptions.DuplicateOfferUrlException;
import com.javaoffers.offer.domain.exceptions.OfferNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OfferService {
    private final OfferRepository repository;

    @Cacheable("all_offers")
    public List<OfferDto> getAllOffers() {
        return repository.findAll()
                .stream()
                .map(OfferMapper::mapFromOfferToOfferDto)
                .collect(Collectors.toList());
    }

    public OfferDto getOfferById(String id) {
        return repository.findById(id)
                .map(OfferMapper::mapFromOfferToOfferDto)
                .orElseThrow(() -> new OfferNotFoundException(id));
    }

    public List<Offer> saveAll(List<Offer> offers) {
        return repository.saveAll(offers);
    }

    public List<Offer> saveAllOffersDto(List<HttpOfferDto> offersDto) {
        List<Offer> offerList = getUniqueOffers(offersDto);
        return repository.saveAll(offerList);
    }

    private List<Offer> getUniqueOffers(List<HttpOfferDto> offersDto) {
        return offersDto.stream()
                .filter(offerDto -> offerDto.getOfferUrl() != null)
                .filter(offerDto -> !offerDto.getOfferUrl().isEmpty())
                .filter(offerDto -> !repository.existsByOfferUrl(offerDto.getOfferUrl()))
                .map(OfferMapper::mapFromHttpOfferDtoToOffer)
                .collect(Collectors.toList());
    }

    public OfferDto saveOffer(OfferDto offerDto) {
        Offer offer = OfferMapper.mapFromOfferDtoToOffer(offerDto);
        try {
            Offer savedOffer = repository.save(offer);
            return OfferMapper.mapFromOfferToOfferDto(savedOffer);
        } catch (DuplicateKeyException e) {
            throw new DuplicateOfferUrlException(offer.getOfferUrl());
        }
    }
}