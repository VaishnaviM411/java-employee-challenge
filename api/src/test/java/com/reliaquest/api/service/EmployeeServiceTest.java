package com.reliaquest.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.reliaquest.api.entity.Employee;
import com.reliaquest.api.exception.HttpException;
import com.reliaquest.api.request.EmployeeCreationRequest;
import com.reliaquest.api.request.EmployeeDeleteRequest;
import com.reliaquest.api.response.EmployeeResponse;
import com.reliaquest.api.response.EmployeeServerResponse;
import com.reliaquest.api.utils.TestEmployeeBuilder;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class EmployeeServiceTest {

    EmployeeServerService employeeServerService = mock(EmployeeServerService.class);
    TestEmployeeBuilder testUtils = new TestEmployeeBuilder();
    EmployeeService classToBeTested = new EmployeeService(employeeServerService);

    @Test
    void shouldGetEmployees() {
        EmployeeServerResponse<List<EmployeeResponse>> allEmployeeResponse =
                testUtils.mockAllEmployeeResponse(testUtils.employeeResponse);
        EmployeeResponse employeeResponse =
                allEmployeeResponse.data().stream().findFirst().get();
        when(employeeServerService.getAllEmployees()).thenReturn(allEmployeeResponse);

        List<Employee> employees = classToBeTested.getEmployees();

        assertEquals(
                List.of(new Employee(
                        employeeResponse.getId(),
                        employeeResponse.getName(),
                        employeeResponse.getSalary(),
                        employeeResponse.getAge(),
                        employeeResponse.getTitle(),
                        employeeResponse.getEmail())),
                employees);
        verify(employeeServerService, times(1)).getAllEmployees();
    }

    @Test
    void shouldSearchEmployeesByName() {
        Employee employee = testUtils.mockEmployeeWithName("John Doe");
        EmployeeServerResponse<List<EmployeeResponse>> allEmployeeResponse =
                testUtils.mockAllEmployeeResponse(testUtils.mockEmployeeResponse(employee));
        when(employeeServerService.getAllEmployees()).thenReturn(allEmployeeResponse);

        List<Employee> employees = classToBeTested.searchEmployeesByName("joh");

        assertEquals(List.of(employee), employees);
        verify(employeeServerService, times(1)).getAllEmployees();
    }

    @Test
    void shouldGetEmployeeById() {
        String id = "id";
        EmployeeServerResponse<EmployeeResponse> singleEmployeeResponse = testUtils.mockSingleEmployeeResponse();
        EmployeeResponse employeeResponse = singleEmployeeResponse.data();
        when(employeeServerService.getEmployeeById(id)).thenReturn(singleEmployeeResponse);

        Employee employee = classToBeTested.getEmployeeById(id);

        assertEquals(
                new Employee(
                        employeeResponse.getId(),
                        employeeResponse.getName(),
                        employeeResponse.getSalary(),
                        employeeResponse.getAge(),
                        employeeResponse.getTitle(),
                        employeeResponse.getEmail()),
                employee);
        verify(employeeServerService, times(1)).getEmployeeById(id);
    }

    @Test
    void shouldThrowErrorIfEmployeeNotFound() {
        String id = "id";
        EmployeeServerResponse<EmployeeResponse> singleEmployeeResponse =
                new EmployeeServerResponse<>(null, EmployeeServerResponse.Status.HANDLED, null);
        when(employeeServerService.getEmployeeById(id))
                .thenThrow(new HttpException(
                        HttpStatus.NOT_FOUND.value(),
                        singleEmployeeResponse.status().getValue(),
                        null));

        HttpException exception = assertThrows(HttpException.class, () -> classToBeTested.getEmployeeById(id));

        assertEquals(HttpStatus.NOT_FOUND.value(), exception.getStatus());
        assertEquals(singleEmployeeResponse.status().getValue(), exception.getErrorMessage());
        verify(employeeServerService, times(1)).getEmployeeById(id);
    }

    @Test
    void shouldReturnHighestSalary() {
        List<Employee> employees = List.of(testUtils.mockEmployee("John", 1000), testUtils.mockEmployee("John", 5000));
        List<EmployeeResponse> employeeResponses = employees.stream()
                .map(employee -> testUtils.mockEmployeeResponse(employee))
                .toList();
        EmployeeServerResponse<List<EmployeeResponse>> employeeServerResponse =
                testUtils.mockAllEmployeeResponse(employeeResponses);
        when(employeeServerService.getAllEmployees()).thenReturn(employeeServerResponse);

        Integer highestSalary = classToBeTested.getHighestSalary();

        assertEquals(employees.stream().mapToInt(Employee::getSalary).max().orElse(0), highestSalary);
        verify(employeeServerService, times(1)).getAllEmployees();
    }

    @Test
    void shouldReturnTopTenHighestEarningEmployeeNames() {
        List<Employee> employees = buildRandomEmployees(15);
        List<EmployeeResponse> employeeResponses = employees.stream()
                .map(employee -> testUtils.mockEmployeeResponse(employee))
                .toList();
        EmployeeServerResponse<List<EmployeeResponse>> allEmployeeResponse =
                testUtils.mockAllEmployeeResponse(employeeResponses);
        when(employeeServerService.getAllEmployees()).thenReturn(allEmployeeResponse);

        List<String> employeeNames = classToBeTested.topTenHighestEarningEmployeeNames();

        assertEquals(
                employees.stream()
                        .sorted(Comparator.comparing(Employee::getSalary).reversed())
                        .limit(10)
                        .map(Employee::getName)
                        .toList(),
                employeeNames);
        verify(employeeServerService, times(1)).getAllEmployees();
    }

    List<Employee> buildRandomEmployees(Integer count) {
        List<Integer> salaries = new Random().ints(count, 0, 1000).boxed().toList();
        return salaries.stream()
                .map(salary -> testUtils.mockEmployee(RandomStringUtils.randomAlphabetic(10), salary))
                .toList();
    }

    @Test
    void shouldReturnTrueIfEmployeeDeleted() {
        String name = "name";
        EmployeeDeleteRequest request = new EmployeeDeleteRequest(name);
        EmployeeServerResponse<Boolean> employeeServerResponse =
                new EmployeeServerResponse<>(true, EmployeeServerResponse.Status.HANDLED, null);
        when(employeeServerService.deleteEmployee(request)).thenReturn(employeeServerResponse);

        assertEquals(true, classToBeTested.deleteEmployee(name));

        verify(employeeServerService, times(1)).deleteEmployee(request);
    }

    @Test
    void shouldReturnFalseIfEmployeeToBeDeletedNotFound() {
        String name = "name";
        EmployeeDeleteRequest request = new EmployeeDeleteRequest(name);
        EmployeeServerResponse<Boolean> employeeServerResponse =
                new EmployeeServerResponse<>(false, EmployeeServerResponse.Status.HANDLED, null);
        when(employeeServerService.deleteEmployee(request)).thenReturn(employeeServerResponse);

        assertEquals(false, classToBeTested.deleteEmployee(name));

        verify(employeeServerService, times(1)).deleteEmployee(request);
    }

    @Test
    void shouldCreateEmployee() {
        EmployeeCreationRequest request = new EmployeeCreationRequest("John", 1220, 18, "SDE");
        EmployeeServerResponse<EmployeeResponse> response =
                new EmployeeServerResponse<>(testUtils.employeeResponse, EmployeeServerResponse.Status.HANDLED, null);
        when(employeeServerService.createEmployee(request)).thenReturn(response);

        Employee employee = classToBeTested.createEmployee(request);

        assertEquals(
                new Employee(
                        response.data().getId(),
                        response.data().getName(),
                        response.data().getSalary(),
                        response.data().getAge(),
                        response.data().getTitle(),
                        response.data().getEmail()),
                employee);
        verify(employeeServerService, times(1)).createEmployee(request);
    }
}
