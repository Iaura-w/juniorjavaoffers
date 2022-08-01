package com.javaoffers.infrastructure.offer.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.javaoffers.config.TestConfig;
import com.javaoffers.infrastructure.RemoteOfferClient;
import com.javaoffers.infrastructure.offer.dto.HttpOfferDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.util.SocketUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;

public class OfferHttpClientIntegrationTest {

    private final int port = SocketUtils.findAvailableTcpPort();
    private final String uri = "http://localhost:" + port + "/offers";
    private WireMockServer wireMockServer;
    private final RemoteOfferClient remoteOfferClient = new TestConfig().offerTestClient(uri, 1000, 1000);

    @BeforeEach
    void setUp() {
        wireMockServer = new WireMockServer(WireMockConfiguration.options().port(port));
        wireMockServer.start();
        WireMock.configureFor(port);
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }

    @Test
    void should_return_one_job_offer() {
        // given
        WireMock.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody(bodyWithOneOffer())));

        // when
        // then
        then(remoteOfferClient.getOffers())
                .containsExactlyInAnyOrderElementsOf(Collections.singletonList(offerDto1()));
    }

    @Test
    void should_return_zero_job_offers() {
        // given
        WireMock.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody(bodyWithZeroOffers())));

        // when
        // then
        then(remoteOfferClient.getOffers()).isEmpty();
    }

    @Test
    void should_return_two_job_offers() {
        // given
        WireMock.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody(bodyWithTwoOffers())));

        // when
        // then
        then(remoteOfferClient.getOffers())
                .containsExactlyInAnyOrderElementsOf(Arrays.asList(offerDto1(), offerDto2()));
    }

    @Test
    void should_throw_internal_server_error_when_internal_server_error() {
        // given
        WireMock.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .withHeader("Content-Type", "application/json")));

        // when
        // then
        thenThrownBy(() -> remoteOfferClient.getOffers())
                .isInstanceOf(ResponseStatusException.class)
                .hasMessage("500 INTERNAL_SERVER_ERROR \"error occurred when using http client\"");
    }

    @Test
    void should_throw_not_found_status_exception_when_status_not_found() {
        // given
        WireMock.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.NOT_FOUND.value())
                        .withHeader("Content-Type", "application/json")));

        // when
        // then
        thenThrownBy(() -> remoteOfferClient.getOffers())
                .isInstanceOf(ResponseStatusException.class)
                .hasMessage("404 NOT_FOUND");
    }

    @Test
    void should_throw_unauthorized_status_exception_when_status_unauthorized() {
        // given
        WireMock.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.UNAUTHORIZED.value())
                        .withHeader("Content-Type", "application/json")));

        // when
        // then
        thenThrownBy(() -> remoteOfferClient.getOffers())
                .isInstanceOf(ResponseStatusException.class)
                .hasMessage("401 UNAUTHORIZED");
    }

    @Test
    void should_return_zero_job_offers_when_response_delay_is_2000_millis() {
        // given
        WireMock.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody(bodyWithZeroOffers())
                        .withFixedDelay(2000)));
        // when
        // then
        then(remoteOfferClient.getOffers()).isEmpty();
    }

    @Test
    void should_return_zero_job_offers_when_status_is_no_content() {
        // given
        WireMock.stubFor(WireMock.get("/offers")
                .willReturn(WireMock.aResponse()
                        .withStatus(HttpStatus.NO_CONTENT.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody(bodyWithZeroOffers())));
        // when
        // then
        then(remoteOfferClient.getOffers()).isEmpty();
    }

    private HttpOfferDto offerDto1() {
        return getOfferDto("Junior Java Developer", "ABC", "4k - 8k PLN", "https://jobs.com/ABC");
    }

    private HttpOfferDto offerDto2() {
        return getOfferDto("Java Developer", "XYZ", "7k - 14k PLN", "https://jobs.com/XYZ");
    }

    private HttpOfferDto getOfferDto(String title, String company, String salary, String url) {
        return HttpOfferDto.builder().title(title).company(company).salary(salary).offerUrl(url).build();
    }

    private String bodyWithZeroOffers() {
        return "[]";
    }

    private String bodyWithOneOffer() {
        return "[{\n" +
                "    \"title\": \"Junior Java Developer\",\n" +
                "    \"company\": \"ABC\",\n" +
                "    \"salary\": \"4k - 8k PLN\",\n" +
                "    \"offerUrl\": \"https://jobs.com/ABC\"\n" +
                "  }]";
    }

    private String bodyWithTwoOffers() {
        return "[{\n" +
                "    \"title\": \"Junior Java Developer\",\n" +
                "    \"company\": \"ABC\",\n" +
                "    \"salary\": \"4k - 8k PLN\",\n" +
                "    \"offerUrl\": \"https://jobs.com/ABC\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"title\": \"Java Developer\",\n" +
                "    \"company\": \"XYZ\",\n" +
                "    \"salary\": \"7k - 14k PLN\",\n" +
                "    \"offerUrl\": \"https://jobs.com/XYZ\"\n" +
                "  }]";
    }
}
