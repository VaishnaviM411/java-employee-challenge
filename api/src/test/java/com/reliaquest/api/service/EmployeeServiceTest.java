package com.reliaquest.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.reliaquest.api.entity.Employee;
import com.reliaquest.api.response.EmployeeResponse;
import com.reliaquest.api.response.EmployeeServerResponse;
import com.reliaquest.api.utils.TestEmployeeBuilder;
import java.util.List;
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
}
