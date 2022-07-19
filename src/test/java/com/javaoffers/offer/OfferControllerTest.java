package com.javaoffers.offer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaoffers.offer.domain.OfferService;
import com.javaoffers.offer.domain.dto.OfferDto;
import com.javaoffers.offer.domain.exceptions.OfferControllerErrorHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
        int id = 828;
        String expected1 = String.format("Offer with id %d was not found", id);
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
}

@Configuration(proxyBeanMethods = false)
class MockMvcConfig implements SampleOfferDto {

    @Bean
    OfferService offerService() {
        return new OfferService() {
            @Override
            public List<OfferDto> getAllOffers() {
                return Arrays.asList(offerDto1(), offerDto2());
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