package com.javaoffers.offer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaoffers.offer.domain.OfferRepository;
import com.javaoffers.offer.domain.OfferService;
import com.javaoffers.offer.domain.dto.OfferDto;
import com.javaoffers.offer.domain.exceptions.DuplicateOfferUrlException;
import com.javaoffers.offer.domain.exceptions.OfferControllerErrorHandler;
import com.javaoffers.offer.domain.exceptions.OfferNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@ContextConfiguration(classes = MockMvcConfig.class)
class OfferControllerTest implements SampleOfferDto {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void should_return_status_ok_when_get_all_offers() throws Exception {
        // given
        List<OfferDto> expectedOffersDto = Arrays.asList(offerDto1(), offerDto2());
        String expected = objectMapper.writeValueAsString(expectedOffersDto);

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/offers"));

        // then
        MvcResult mvcResult = resultActions
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        String actual = mvcResult.getResponse().getContentAsString();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void should_return_status_ok_when_get_offer_by_id_found() throws Exception {
        // given
        String expected = objectMapper.writeValueAsString(offerDto1());

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/offers/1"));

        // then
        MvcResult mvcResult = resultActions
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
        String actual = mvcResult.getResponse().getContentAsString();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void should_return_status_not_found_when_get_offer_by_id_not_found() throws Exception {
        // given
        String id = "828";
        String expected1 = String.format("Offer with id %s was not found", id);
        String expected2 = HttpStatus.NOT_FOUND.name();

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/offers/" + id));

        // then
        MvcResult mvcResult = resultActions
                .andExpect(status().isNotFound())
                .andDo(print())
                .andReturn();
        String actual = mvcResult.getResponse().getContentAsString();

        assertThat(actual).contains(expected1, expected2);
    }

    @Test
    void should_return_status_created_when_add_offer_valid() throws Exception {
        // given
        String expectedBody = objectMapper.writeValueAsString(newOfferDto());
        String expectedLocation = "http://localhost/api/offers/" + newOfferDto().getId();

        // when
        ResultActions resultActions = mockMvc.perform(post("/api/offers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(expectedBody));

        // then
        MvcResult mvcResult = resultActions
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn();
        String actualBody = mvcResult.getResponse().getContentAsString();
        String actualLocation = mvcResult.getResponse().getHeader("Location");

        assertThat(actualBody).isEqualTo(expectedBody);
        assertThat(actualLocation).isEqualTo(expectedLocation);
    }

    @Test
    void should_return_status_conflict_when_add_offer_urlOffer_already_exists() throws Exception {
        // given
        OfferDto notUniqueOffer = OfferDto.builder()
                .title("title")
                .company("company")
                .salary("salary")
                .offerUrl("not-unique-url")
                .build();
        String content = objectMapper.writeValueAsString(notUniqueOffer);

        String expectedMessage = String.format("Offer with url '%s' already exists", notUniqueOffer.getOfferUrl());
        String expectedStatus = HttpStatus.CONFLICT.name();

        // when
        ResultActions resultActions = mockMvc.perform(post("/api/offers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content));

        // then
        MvcResult mvcResult = resultActions
                .andExpect(status().isConflict())
                .andDo(print())
                .andReturn();
        String actual = mvcResult.getResponse().getContentAsString();

        assertThat(actual).contains(expectedMessage, expectedStatus);
    }

    @Test
    void should_return_status_when_add_offer_not_valid() throws Exception {
        // given
        OfferDto notValidOffer = OfferDto.builder()
                .title(" ")
                .company("company")
                .salary(" ")
                .build();
        String content = objectMapper.writeValueAsString(notValidOffer);

        List<String> expectedMessage = Arrays.asList(
                "title - must not be blank",
                "salary - must not be blank",
                "offerUrl - must not be blank");
        String expectedStatus = HttpStatus.BAD_REQUEST.name();

        // when
        ResultActions resultActions = mockMvc.perform(post("/api/offers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content));

        // then
        MvcResult mvcResult = resultActions
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn();
        String actualBody = mvcResult.getResponse().getContentAsString();

        assertThat(actualBody).contains(expectedMessage).contains(expectedStatus);
    }
}

class MockMvcConfig implements SampleOfferDto {

    @Bean
    OfferService offerService() {
        OfferRepository repository = mock(OfferRepository.class);
        return new OfferService(repository) {
            @Override
            public List<OfferDto> getAllOffers() {
                return Arrays.asList(offerDto1(), offerDto2());
            }

            @Override
            public OfferDto getOfferById(String id) {
                if ("1".equals(id)) {
                    return offerDto1();
                } else if ("2".equals(id)) {
                    return offerDto2();
                }
                throw new OfferNotFoundException(id);
            }

            @Override
            public OfferDto saveOffer(OfferDto offerDto) {
                if (offerDto.getOfferUrl().contains("not-unique-url")) {
                    throw new DuplicateOfferUrlException(offerDto.getOfferUrl());
                }
                return newOfferDto();
            }
        };
    }

    @Bean
    OfferController offerController(OfferService offerService) {
        return new OfferController(offerService);
    }

    @Bean
    OfferControllerErrorHandler offerControllerErrorHandler() {
        return new OfferControllerErrorHandler();
    }
}