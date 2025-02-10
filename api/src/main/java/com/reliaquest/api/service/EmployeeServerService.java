package com.reliaquest.api.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reliaquest.api.exception.HttpException;
import com.reliaquest.api.request.EmployeeCreationRequest;
import com.reliaquest.api.request.EmployeeDeleteRequest;
import com.reliaquest.api.response.EmployeeResponse;
import com.reliaquest.api.response.EmployeeServerResponse;
import java.net.http.HttpResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeServerService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private String host;
    private String baseUrl;
    private String port;
    private HttpService httpService;

    @Autowired
    public EmployeeServerService(
            @Value("${mock-employee-server.host}") String host,
            @Value("${mock-employee-server.base-url}") String baseUrl,
            @Value("${mock-employee-server.port}") String port,
            HttpService httpService) {
        this.host = host;
        this.baseUrl = baseUrl;
        this.port = port;
        this.httpService = httpService;
    }

    public EmployeeServerResponse<List<EmployeeResponse>> getAllEmployees() throws Exception {
        HttpResponse<String> response = httpService.makeHttpRequest(HttpMethod.GET.name(), getEmployeeServerUrl(), "");
        return objectMapper.readValue(response.body(), new TypeReference<>() {});
    }

    public EmployeeServerResponse<EmployeeResponse> getEmployeeById(String id) {
        HttpResponse<String> response =
                httpService.makeHttpRequest(HttpMethod.GET.name(), getEmployeeServerUrl() + "/" + id, "");

        return readValueFromJson(response);
    }

    public EmployeeServerResponse<Boolean> deleteEmployee(EmployeeDeleteRequest employeeDeleteRequest)
            throws Exception {
        HttpResponse<String> response = httpService.makeHttpRequest(
                HttpMethod.DELETE.name(),
                getEmployeeServerUrl(),
                objectMapper.writeValueAsString(employeeDeleteRequest));

        return objectMapper.readValue(response.body(), new TypeReference<>() {});
    }

    public EmployeeServerResponse<EmployeeResponse> createEmployee(EmployeeCreationRequest employeeCreationRequest)
            throws Exception {
        HttpResponse<String> response = httpService.makeHttpRequest(
                HttpMethod.POST.name(),
                getEmployeeServerUrl(),
                objectMapper.writeValueAsString(employeeCreationRequest));

        return objectMapper.readValue(response.body(), new TypeReference<>() {});
    }

    private String getEmployeeServerUrl() {
        return "http://" + host + ":" + port + baseUrl;
    }

    private EmployeeServerResponse<EmployeeResponse> readValueFromJson(HttpResponse<String> response) {
        try {
            return objectMapper.readValue(response.body(), new TypeReference<>() {});
        } catch (Exception exception) {
            throw new HttpException(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error occurred while parsing json response", exception);
        }
    }
}
