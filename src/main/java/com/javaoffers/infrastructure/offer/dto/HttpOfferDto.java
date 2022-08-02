package com.javaoffers.infrastructure.offer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HttpOfferDto {
    private String title;
    private String company;
    private String salary;
    private String offerUrl;
}
