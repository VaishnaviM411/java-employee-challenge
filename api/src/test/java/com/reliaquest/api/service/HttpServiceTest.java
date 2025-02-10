package com.reliaquest.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.verify.VerificationTimes.exactly;

import com.reliaquest.api.exception.HttpException;
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
    private final HttpService classToBeTested = new HttpService(timeOutInSec);
    private ClientAndServer mockClient;

    @BeforeEach
    void setUp() {
        mockClient = ClientAndServer.startClientAndServer(port);
    }

    @AfterEach
    void tearDown() {
        mockClient.stop();
    }

    @Test
    void shouldMakeGetRequest() {
        HttpRequest get = request().withMethod(HttpMethod.GET.name());
        mockClient
                .when(get)
                .respond(response()
                        .withStatusCode(HttpStatus.OK.value())
                        .withHeader(Header.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString()))
                        .withBody(responseBody));

        HttpResponse<String> response = classToBeTested.makeHttpRequest(HttpMethod.GET.name(), mockUri, "");

        assertEquals(HttpStatus.OK.value(), response.statusCode());
        assertEquals(responseBody, response.body());
        mockClient.verify(get, exactly(1));
    }

    @Test
    void shouldThrowHttpExceptionOnFailedCall() {
        HttpRequest get = request().withMethod(HttpMethod.GET.name());
        HttpStatus errorStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        String errorMessage = "error";
        mockClient
                .when(get)
                .respond(response()
                        .withStatusCode(errorStatus.value())
                        .withHeader(Header.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString()))
                        .withBody(errorMessage));

        HttpException error = assertThrows(
                HttpException.class, () -> classToBeTested.makeHttpRequest(HttpMethod.GET.name(), mockUri, ""));

        assertEquals(errorStatus.value(), error.getStatus());
        assertEquals(errorMessage, error.getErrorMessage());
        mockClient.verify(get, exactly(1));
    }

    @Test
    void shouldMakeDeleteRequest() {
        String requestBody = "requestBody";
        HttpRequest delete = request().withMethod(HttpMethod.DELETE.name()).withBody(requestBody);
        mockClient
                .when(delete)
                .respond(response()
                        .withStatusCode(HttpStatus.OK.value())
                        .withHeader(Header.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString()))
                        .withBody(responseBody));

        HttpResponse<String> response = classToBeTested.makeHttpRequest(HttpMethod.DELETE.name(), mockUri, requestBody);

        assertEquals(HttpStatus.OK.value(), response.statusCode());
        assertEquals(responseBody, response.body());
        mockClient.verify(delete, exactly(1));
    }

    @Test
    void shouldMakePostRequest() {
        String requestBody = "requestBody";
        HttpRequest post = request().withMethod(HttpMethod.POST.name()).withBody(requestBody);
        mockClient
                .when(post)
                .respond(response()
                        .withStatusCode(HttpStatus.OK.value())
                        .withHeader(Header.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString()))
                        .withBody(responseBody));

        HttpResponse<String> response = classToBeTested.makeHttpRequest(HttpMethod.POST.name(), mockUri, requestBody);

        assertEquals(HttpStatus.OK.value(), response.statusCode());
        assertEquals(responseBody, response.body());
        mockClient.verify(post, exactly(1));
    }
}
