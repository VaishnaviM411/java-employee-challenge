package com.reliaquest.api.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeCreationRequest {
    @NotBlank(message = "Name should not be blank")
    String name;

    @Min(value = 0, message = "Salary should be greater than zero")
    Integer salary;

    @Min(value = 16, message = "Minimum age 16 required")
    @Max(value = 75, message = "Maximum age 75 required")
    Integer age;

    @NotBlank(message = "Title should not be blank")
    String title;
}
