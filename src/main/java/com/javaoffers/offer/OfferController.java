package com.javaoffers.offer;

import com.javaoffers.offer.domain.OfferMapper;
import com.javaoffers.offer.domain.dto.OfferDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1")
@Slf4j
public class OfferController {
    private final Map<Long, OfferDto> data = new HashMap<>();

    public OfferController() {
        data.put(1L, OfferMapper.mapToOfferDto("Junior Java Developer", "ABC", "6k - 8k PLN", "https://example.com"));
        data.put(2L, OfferMapper.mapToOfferDto("Junior Android Developer", "XYZ", "7k - 10k PLN", "https://example.com"));
    }

    @GetMapping("/offers")
    public ResponseEntity<List<OfferDto>> getAllOffers() {
        List<OfferDto> list = new ArrayList<>(data.values());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/offers/{id}")
    public ResponseEntity<OfferDto> getOfferById(@PathVariable Long id) {
        if (!data.containsKey(id)) {
            String message = "offer with id " + id + " does not exists";
            log.error(message);
            return ResponseEntity.notFound().build();
        }
        OfferDto offerDto = data.get(id);
        return ResponseEntity.ok(offerDto);
    }
}