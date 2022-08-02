package com.javaoffers.offer.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("offers")
public class Offer {
    @Id
    String id;
    @Field
    String company;
    @Field
    String title;
    @Field
    String salary;
    @Field
    String offerUrl;
}