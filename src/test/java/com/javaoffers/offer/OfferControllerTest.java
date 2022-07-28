package com.javaoffers.offer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaoffers.offer.domain.OfferRepository;
import com.javaoffers.offer.domain.OfferService;
import com.javaoffers.offer.domain.dto.OfferDto;
import com.javaoffers.offer.domain.exceptions.OfferControllerErrorHandler;
import com.javaoffers.offer.domain.exceptions.OfferNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
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