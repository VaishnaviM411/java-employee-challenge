package com.reliaquest.api.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.reliaquest.api.entity.Employee;
import com.reliaquest.api.request.EmployeeCreationRequest;
import com.reliaquest.api.service.EmployeeService;
import com.reliaquest.api.utils.TestEmployeeBuilder;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class EmployeeControllerTest {

    EmployeeService mockEmployeeService = mock(EmployeeService.class);
    EmployeeController employeeController = new EmployeeController(mockEmployeeService);

    TestEmployeeBuilder testUtil = new TestEmployeeBuilder();

    @BeforeEach
    void setUp() {
        reset(mockEmployeeService);
    }

    @Test
    void shouldGetAllEmployees() throws Exception {
        List<Employee> expectedResult = List.of(testUtil.mockEmployeeWithName("John Doe"));
        when(mockEmployeeService.getEmployees()).thenReturn(expectedResult);

        ResponseEntity<List<Employee>> employees = employeeController.getAllEmployees();

        assertEquals(new ResponseEntity<>(expectedResult, HttpStatus.OK), employees);
        verify(mockEmployeeService, times(1)).getEmployees();
    }

    @Test
    void shouldSearchEmployeeByName() throws Exception {
        String searchString = "Joh";
        List<Employee> expectedResult = List.of(testUtil.mockEmployeeWithName("John Doe"));
        when(mockEmployeeService.searchEmployeesByName(searchString)).thenReturn(expectedResult);

        ResponseEntity<List<Employee>> employees = employeeController.getEmployeesByNameSearch(searchString);

        assertEquals(new ResponseEntity<>(expectedResult, HttpStatus.OK), employees);
        verify(mockEmployeeService, times(1)).searchEmployeesByName(searchString);
    }

    @Test
    void shouldGetEmployeeById() throws Exception {
        String id = "id";
        Employee expectedResult = testUtil.mockEmployee;
        when(mockEmployeeService.getEmployeeById(id)).thenReturn(expectedResult);

        ResponseEntity<Employee> employee = employeeController.getEmployeeById(id);

        assertEquals(new ResponseEntity<>(expectedResult, HttpStatus.OK), employee);
        verify(mockEmployeeService, times(1)).getEmployeeById(id);
    }

    @Test
    void shouldGetHighestSalary() throws Exception {
        Integer expectedResult = 1000;
        when(mockEmployeeService.getHighestSalary()).thenReturn(expectedResult);

        ResponseEntity<Integer> highestSalary = employeeController.getHighestSalaryOfEmployees();

        assertEquals(new ResponseEntity<>(expectedResult, HttpStatus.OK), highestSalary);
        verify(mockEmployeeService, times(1)).getHighestSalary();
    }

    @Test
    void shouldGetTopTenHighestEarningEmployeeNames() throws Exception {
        List<String> expectedResult = List.of("John");
        when(mockEmployeeService.topTenHighestEarningEmployeeNames()).thenReturn(expectedResult);

        ResponseEntity<List<String>> names = employeeController.getTopTenHighestEarningEmployeeNames();

        assertEquals(new ResponseEntity<>(expectedResult, HttpStatus.OK), names);
        verify(mockEmployeeService, times(1)).topTenHighestEarningEmployeeNames();
    }

    @Test
    void shouldDeleteEmployeeIfExists() throws Exception {
        Employee employee = testUtil.mockEmployee;
        when(mockEmployeeService.getEmployeeById(employee.getId().toString())).thenReturn(employee);
        when(mockEmployeeService.deleteEmployee(employee.getName())).thenReturn(true);

        ResponseEntity<String> deletedEmployeeName =
                employeeController.deleteEmployeeById(employee.getId().toString());

        assertEquals(new ResponseEntity<>(employee.getName(), HttpStatus.OK), deletedEmployeeName);
        verify(mockEmployeeService, times(1)).getEmployeeById(employee.getId().toString());
        verify(mockEmployeeService, times(1)).deleteEmployee(employee.getName());
    }

    @Test
    void shouldReturnBadRequestIfEmployeeDeletionFails() throws Exception {
        Employee employee = testUtil.mockEmployee;
        when(mockEmployeeService.getEmployeeById(employee.getId().toString())).thenReturn(employee);
        when(mockEmployeeService.deleteEmployee(employee.getName())).thenReturn(false);

        ResponseEntity<String> response =
                employeeController.deleteEmployeeById(employee.getId().toString());

        assertEquals(
                new ResponseEntity<>(
                        "Employee deletion failed with name " + employee.getName(), HttpStatus.BAD_REQUEST),
                response);
        verify(mockEmployeeService, times(1)).getEmployeeById(employee.getId().toString());
        verify(mockEmployeeService, times(1)).deleteEmployee(employee.getName());
    }

    @Test
    void shouldCreateEmployeeWithValidRequest() throws Exception {
        EmployeeCreationRequest request = new EmployeeCreationRequest("John", 1220, 18, "SDE");
        when(mockEmployeeService.createEmployee(request)).thenReturn(testUtil.mockEmployee);

        ResponseEntity<Employee> response = employeeController.createEmployee(request);

        assertEquals(new ResponseEntity<>(testUtil.mockEmployee, HttpStatus.OK), response);
        verify(mockEmployeeService, times(1)).createEmployee(request);
    }
}
