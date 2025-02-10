package com.reliaquest.api.utils;

import com.reliaquest.api.entity.Employee;
import com.reliaquest.api.response.EmployeeResponse;
import com.reliaquest.api.response.EmployeeServerResponse;
import java.util.List;
import java.util.UUID;

public class TestEmployeeBuilder {

    public Employee mockEmployee = new Employee(UUID.randomUUID(), "John Doe", 2000, 18, "SDE", "john@work.com");

    public Employee mockEmployee(String name, Integer salary) {
        this.mockEmployee.setName(name);
        this.mockEmployee.setSalary(salary);
        return this.mockEmployee;
    }

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

    public EmployeeResponse mockEmployeeResponse(Employee employee) {
        return new EmployeeResponse(
                employee.getId(),
                employee.getName(),
                employee.getSalary(),
                employee.getAge(),
                employee.getTitle(),
                employee.getEmail());
    }

    public EmployeeServerResponse<List<EmployeeResponse>> mockAllEmployeeResponse(EmployeeResponse employeeResponse) {
        return new EmployeeServerResponse<>(List.of(employeeResponse), EmployeeServerResponse.Status.HANDLED, null);
    }

    public EmployeeServerResponse<List<EmployeeResponse>> mockAllEmployeeResponse(List<EmployeeResponse> employees) {
        return new EmployeeServerResponse<>(employees, EmployeeServerResponse.Status.HANDLED, null);
    }

    public EmployeeServerResponse<EmployeeResponse> mockSingleEmployeeResponse() {
        return new EmployeeServerResponse<>(employeeResponse, EmployeeServerResponse.Status.HANDLED, null);
    }
}
