package com.reliaquest.api.entity;

import com.reliaquest.api.response.EmployeeResponse;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    private UUID id;
    private String name;
    private Integer salary;
    private Integer age;
    private String title;
    private String email;

    public static Employee from(EmployeeResponse employeeResponse) {
        return new Employee(
                employeeResponse.getId(),
                employeeResponse.getName(),
                employeeResponse.getSalary(),
                employeeResponse.getAge(),
                employeeResponse.getTitle(),
                employeeResponse.getEmail());
    }
}
