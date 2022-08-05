package com.javaoffers.offer;

import com.javaoffers.offer.domain.OfferService;
import com.javaoffers.offer.domain.dto.OfferDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/offers")
@RequiredArgsConstructor
@Slf4j
public class OfferController {
    private final OfferService service;

    @GetMapping
    public ResponseEntity<List<OfferDto>> getAllOffers() {
        log.info("Request for all offers");
        return ResponseEntity.ok(service.getAllOffers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OfferDto> getOfferById(@PathVariable String id) {
        log.info("Request for offer with id " + id);
        return ResponseEntity.ok(service.getOfferById(id));
    }

    @PostMapping
    public ResponseEntity<OfferDto> addOffer(@RequestBody OfferDto offerDto) {
        OfferDto savedOffer = service.saveOffer(offerDto);
        log.info("Added new offer");
        return ResponseEntity.ok(savedOffer);
    }
}