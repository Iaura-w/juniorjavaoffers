package com.javaoffers.infrastructure.offer.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

public class RestTemplateResponseErrorHandler extends DefaultResponseErrorHandler {
    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        final HttpStatus statusCode = response.getStatusCode();

        if (statusCode.is5xxServerError()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "error occurred when using http client");
        } else if (statusCode.is4xxClientError()) {
            if (statusCode == HttpStatus.NOT_FOUND) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            } else if (statusCode == HttpStatus.UNAUTHORIZED) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
            }
        } else {
            throw new ResponseStatusException(statusCode, statusCode.getReasonPhrase());
        }
    }
}
