package com.reliaquest.api.utils;

import com.reliaquest.api.response.EmployeeResponse;
import java.util.UUID;

public class TestEmployeeBuilder {

    public EmployeeResponse employeeResponse =
            new EmployeeResponse(UUID.randomUUID(), "John Doe", 2000, 18, "SDE", "john@work.com");

    public EmployeeResponse mockEmployeeResponseWithName(String name) {
        this.employeeResponse.setName(name);
        return this.employeeResponse;
    }
}
