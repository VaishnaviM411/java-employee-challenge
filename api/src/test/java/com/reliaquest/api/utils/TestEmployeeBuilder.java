package com.reliaquest.api.utils;

import com.reliaquest.api.entity.Employee;
import com.reliaquest.api.response.EmployeeResponse;
import com.reliaquest.api.response.EmployeeServerResponse;
import java.util.List;
import java.util.UUID;

public class TestEmployeeBuilder {

    public Employee mockEmployee = new Employee(UUID.randomUUID(), "John Doe", 2000, 18, "SDE", "john@work.com");
    public EmployeeResponse employeeResponse =
            new EmployeeResponse(UUID.randomUUID(), "John Doe", 2000, 18, "SDE", "john@work.com");

    public Employee mockEmployeeWithName(String name) {
        this.mockEmployee.setName(name);
        return this.mockEmployee;
    }

    public EmployeeResponse mockEmployeeResponseWithName(String name) {
        this.employeeResponse.setName(name);
        return this.employeeResponse;
    }

    public EmployeeServerResponse<List<EmployeeResponse>> mockAllEmployeeResponse(EmployeeResponse employeeResponse) {
        return new EmployeeServerResponse<>(List.of(employeeResponse), EmployeeServerResponse.Status.HANDLED, null);
    }
}
