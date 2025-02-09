package com.reliaquest.api.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.reliaquest.api.entity.Employee;
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
}
