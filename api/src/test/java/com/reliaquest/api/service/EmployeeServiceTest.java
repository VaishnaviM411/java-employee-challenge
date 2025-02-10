package com.reliaquest.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.reliaquest.api.entity.Employee;
import com.reliaquest.api.response.EmployeeResponse;
import com.reliaquest.api.response.EmployeeServerResponse;
import com.reliaquest.api.utils.TestEmployeeBuilder;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

class EmployeeServiceTest {

    EmployeeServerService employeeServerService = mock(EmployeeServerService.class);
    TestEmployeeBuilder testUtils = new TestEmployeeBuilder();
    EmployeeService classToBeTested = new EmployeeService(employeeServerService);

    @Test
    void shouldGetEmployees() throws Exception {
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
    void shouldSearchEmployeesByName() throws Exception {
        Employee employee = testUtils.mockEmployeeWithName("John Doe");
        EmployeeServerResponse<List<EmployeeResponse>> allEmployeeResponse =
                testUtils.mockAllEmployeeResponse(testUtils.mockEmployeeResponse(employee));
        when(employeeServerService.getAllEmployees()).thenReturn(allEmployeeResponse);

        List<Employee> employees = classToBeTested.searchEmployeesByName("joh");

        assertEquals(List.of(employee), employees);
        verify(employeeServerService, times(1)).getAllEmployees();
    }

    @Test
    void shouldReturnHighestSalary() throws Exception {
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
    void shouldReturnTopTenHighestEarningEmployeeNames() throws Exception {
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
}
