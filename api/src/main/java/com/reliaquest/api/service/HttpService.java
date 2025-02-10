package com.reliaquest.api.service;

import com.reliaquest.api.exception.HttpException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class HttpService {
    private final HttpClient httpClient;

    @Autowired
    public HttpService(@Value("${http.timeout-in-sec}") Integer timeOutInSec) {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(timeOutInSec))
                .build();
    }

    private static boolean isRequestSuccessful(HttpResponse<String> response) {
        return response.statusCode() != HttpStatus.OK.value();
    }

    public HttpResponse<String> makeHttpRequest(String method, String url, String body) {
        try {
            logRequest(url, body);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .headers(
                            HttpHeaders.ACCEPT,
                            MediaType.APPLICATION_JSON_VALUE,
                            HttpHeaders.CONTENT_TYPE,
                            MediaType.APPLICATION_JSON_VALUE)
                    .method(method, HttpRequest.BodyPublishers.ofString(body))
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (isRequestSuccessful(response)) {
                logFailedRequest(response);
                throw new HttpException(response.statusCode(), response.body(), null);
            }

            logResponse(response);
            return response;
        } catch (HttpException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new HttpException(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage(), exception);
        }
    }

    private void logFailedRequest(HttpResponse<String> response) {
        log.error("API call failed with status code " + response.statusCode() + " error " + response.body());
    }

    private void logRequest(String url, String body) {
        log.info(String.format("API Request - URL:%s, Body:%s", url, body));
    }

    private void logResponse(HttpResponse<String> response) {
        log.info(String.format("API Response - Status:%s, Body:%s", response.statusCode(), response.body()));
    }
}
