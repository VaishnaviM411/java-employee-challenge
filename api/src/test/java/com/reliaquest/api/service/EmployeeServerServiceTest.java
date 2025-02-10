package com.reliaquest.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reliaquest.api.exception.HttpException;
import com.reliaquest.api.request.EmployeeDeleteRequest;
import com.reliaquest.api.response.EmployeeResponse;
import com.reliaquest.api.response.EmployeeServerResponse;
import com.reliaquest.api.utils.HttpResponseImpl;
import com.reliaquest.api.utils.TestEmployeeBuilder;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

class EmployeeServerServiceTest {

    private final String host = "localhost";
    private final String baseUrl = "/employee";
    private final String port = "3000";
    private final ObjectMapper objectMapper = new ObjectMapper();
    EmployeeServerService classToBeTested;
    HttpService mockHttpService = mock(HttpService.class);
    String mockEmployeeServerBaseUrl = "http://" + host + ":" + port + baseUrl;
    TestEmployeeBuilder testUtils = new TestEmployeeBuilder();

    @BeforeEach
    void setUp() {
        classToBeTested = new EmployeeServerService(host, baseUrl, port, mockHttpService);
    }

    @Test
    void shouldCallGetAllEmployeesApi() throws Exception {
        EmployeeServerResponse<List<EmployeeResponse>> response = new EmployeeServerResponse<>(
                List.of(testUtils.mockEmployeeResponseWithName("John Doe")),
                EmployeeServerResponse.Status.HANDLED,
                null);
        when(mockHttpService.makeHttpRequest(HttpMethod.GET.name(), mockEmployeeServerBaseUrl, ""))
                .thenReturn(HttpResponseImpl.build(HttpStatus.OK, objectMapper.writeValueAsString(response)));

        assertEquals(response, classToBeTested.getAllEmployees());

        verify(mockHttpService, times(1)).makeHttpRequest(HttpMethod.GET.name(), mockEmployeeServerBaseUrl, "");
    }

    @Test
    void shouldCallGetEmployeeByIdApi() throws Exception {
        String id = "id";
        EmployeeServerResponse<EmployeeResponse> singleEmployeeResponse = testUtils.mockSingleEmployeeResponse();
        String url = mockEmployeeServerBaseUrl + "/" + id;
        when(mockHttpService.makeHttpRequest(HttpMethod.GET.name(), url, ""))
                .thenReturn(
                        HttpResponseImpl.build(HttpStatus.OK, objectMapper.writeValueAsString(singleEmployeeResponse)));

        assertEquals(singleEmployeeResponse, classToBeTested.getEmployeeById(id));

        verify(mockHttpService, times(1)).makeHttpRequest(HttpMethod.GET.name(), url, "");
    }

    @Test
    void shouldThrowErrorIfEmployeeNotFound() throws Exception {
        String id = "id";
        EmployeeServerResponse<EmployeeResponse> singleEmployeeResponse =
                new EmployeeServerResponse<>(null, EmployeeServerResponse.Status.HANDLED, null);
        String url = mockEmployeeServerBaseUrl + "/" + id;
        when(mockHttpService.makeHttpRequest(HttpMethod.GET.name(), url, ""))
                .thenThrow(new HttpException(
                        HttpStatus.NOT_FOUND.value(),
                        singleEmployeeResponse.status().getValue(),
                        null));

        HttpException exception = assertThrows(HttpException.class, () -> classToBeTested.getEmployeeById(id));

        assertEquals(HttpStatus.NOT_FOUND.value(), exception.getStatus());
        assertEquals(singleEmployeeResponse.status().getValue(), exception.getErrorMessage());
        verify(mockHttpService, times(1)).makeHttpRequest(HttpMethod.GET.name(), url, "");
    }

    @Test
    void shouldCallDeleteEmployeeApi() throws Exception {
        EmployeeDeleteRequest requestBody = new EmployeeDeleteRequest("John");
        EmployeeServerResponse<Boolean> response =
                new EmployeeServerResponse<>(true, EmployeeServerResponse.Status.HANDLED, null);
        when(mockHttpService.makeHttpRequest(
                        HttpMethod.DELETE.name(),
                        mockEmployeeServerBaseUrl,
                        objectMapper.writeValueAsString(requestBody)))
                .thenReturn(HttpResponseImpl.build(HttpStatus.OK, objectMapper.writeValueAsString(response)));

        assertEquals(response, classToBeTested.deleteEmployee(requestBody));

        verify(mockHttpService, times(1))
                .makeHttpRequest(
                        HttpMethod.DELETE.name(),
                        mockEmployeeServerBaseUrl,
                        objectMapper.writeValueAsString(requestBody));
    }
}
