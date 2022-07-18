package com.javaoffers.offer;

import com.javaoffers.offer.domain.OfferService;
import com.javaoffers.offer.domain.dto.OfferDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/offers")
@RequiredArgsConstructor
public class OfferController {

    private final OfferService service;

    @GetMapping
    public ResponseEntity<List<OfferDto>> getAllOffers() {
        return ResponseEntity.ok(service.findAllOffers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OfferDto> getOfferById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findOfferById(id));
    }
}