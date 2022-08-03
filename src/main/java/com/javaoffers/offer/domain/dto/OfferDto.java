package com.javaoffers.offer.domain.dto;

import lombok.Builder;
import lombok.Value;

import java.io.Serializable;

@Value
@Builder
public class OfferDto implements Serializable {
    String id;
    String title;
    String company;
    String salary;
    String offerUrl;
}