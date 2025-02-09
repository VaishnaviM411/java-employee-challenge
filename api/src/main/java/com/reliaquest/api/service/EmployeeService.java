package com.reliaquest.api.service;

import com.reliaquest.api.entity.Employee;
import com.reliaquest.api.response.EmployeeResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeServerService employeeServerService;

    public List<Employee> getEmployees() throws Exception {
        List<EmployeeResponse> data = employeeServerService.getAllEmployees().data();
        return data.stream().map(Employee::from).toList();
    }

    public List<Employee> searchEmployeesByName(String searchString) throws Exception {
        return getEmployees().stream()
                .filter(it -> it.getName().toLowerCase().contains(searchString.toLowerCase()))
                .toList();
    }
}
