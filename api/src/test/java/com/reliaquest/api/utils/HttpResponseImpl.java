package com.reliaquest.api.utils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;
import javax.net.ssl.SSLSession;
import org.springframework.http.HttpStatus;

public class HttpResponseImpl<T> implements HttpResponse<T> {
    private final HttpStatus statusCode;

    private final T body;

    public HttpResponseImpl(HttpStatus statusCode, T body) {

        this.statusCode = statusCode;
        this.body = body;
    }

    public static <T> HttpResponseImpl<T> build(HttpStatus statusCode, T body) {
        return new HttpResponseImpl<>(statusCode, body);
    }

    @Override
    public int statusCode() {
        return statusCode.value();
    }

    @Override
    public HttpRequest request() {
        return null;
    }

    @Override
    public Optional<HttpResponse<T>> previousResponse() {
        return Optional.empty();
    }

    @Override
    public HttpHeaders headers() {
        return null;
    }

    @Override
    public T body() {
        return body;
    }

    @Override
    public Optional<SSLSession> sslSession() {
        return Optional.empty();
    }

    @Override
    public URI uri() {
        return null;
    }

    @Override
    public HttpClient.Version version() {
        return null;
    }
}
