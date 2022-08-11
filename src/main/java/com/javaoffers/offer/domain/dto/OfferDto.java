package com.javaoffers.offer.domain.dto;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Value
@Builder
public class OfferDto implements Serializable {
    String id;
    @NotBlank
    String title;
    @NotBlank
    String company;
    @NotBlank
    String salary;
    @NotBlank
    String offerUrl;
}