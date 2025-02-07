package com.reliaquest.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.verify.VerificationTimes.exactly;

import java.net.http.HttpResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Header;
import org.mockserver.model.HttpRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class HttpServiceTest {

    Integer port = 3000;
    String mockUri = "http://localhost:" + port;
    String responseBody = "response";
    Integer timeOutInSec = 60;
    private ClientAndServer mockClient;
    private final HttpService classToBeTested = new HttpService(timeOutInSec);

    @BeforeEach
    void setUp() {
        mockClient = ClientAndServer.startClientAndServer(port);
    }

    @AfterEach
    void tearDown() {
        mockClient.stop();
    }

    @Test
    void shouldMakeGetRequest() throws Exception {
        HttpRequest get = request().withMethod(HttpMethod.GET.name());
        mockClient
                .when(get)
                .respond(response()
                        .withStatusCode(200)
                        .withHeader(Header.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString()))
                        .withBody(responseBody));

        HttpResponse<String> response = classToBeTested.makeHttpRequest(HttpMethod.GET.name(), mockUri, "");

        assertEquals(HttpStatus.OK.value(), response.statusCode());
        assertEquals(responseBody, response.body());
        mockClient.verify(get, exactly(1));
    }
}
