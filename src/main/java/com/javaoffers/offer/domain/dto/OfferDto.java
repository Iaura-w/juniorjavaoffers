package com.javaoffers.offer.domain.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class OfferDto {
    String id;
    String title;
    String company;
    String salary;
    String offerUrl;
}