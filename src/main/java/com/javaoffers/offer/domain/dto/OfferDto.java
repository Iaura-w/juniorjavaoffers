package com.javaoffers.offer.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class OfferDto {
    private String title;
    private String company;
    private String salary;
    private String offerUrl;
}