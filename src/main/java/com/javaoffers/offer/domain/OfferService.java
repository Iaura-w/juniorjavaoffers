package com.javaoffers.offer.domain;

import com.javaoffers.offer.domain.dto.OfferDto;
import com.javaoffers.offer.domain.exceptions.OfferNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OfferService {
    private final Map<Long, OfferDto> data = new HashMap<>();

    public OfferService() {
        data.put(1L, OfferMapper.mapToOfferDto("Junior Java Developer", "ABC", "6k - 8k PLN", "https://example.com"));
        data.put(2L, OfferMapper.mapToOfferDto("Junior Android Developer", "XYZ", "7k - 10k PLN", "https://example.com"));
    }

    public List<OfferDto> findAllOffers() {
        return new ArrayList<>(data.values());
    }

    public OfferDto findOfferById(long id) {
        if (!data.containsKey(id)) {
            throw new OfferNotFoundException(id);
        } else {
            return data.get(id);
        }
    }
}