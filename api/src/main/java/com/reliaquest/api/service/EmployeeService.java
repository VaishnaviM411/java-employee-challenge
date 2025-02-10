package com.reliaquest.api.service;

import com.reliaquest.api.entity.Employee;
import com.reliaquest.api.request.EmployeeCreationRequest;
import com.reliaquest.api.request.EmployeeDeleteRequest;
import com.reliaquest.api.response.EmployeeResponse;
import com.reliaquest.api.response.EmployeeServerResponse;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeServerService employeeServerService;

    public List<Employee> getEmployees() {
        List<EmployeeResponse> data = employeeServerService.getAllEmployees().data();
        return data.stream().map(Employee::from).toList();
    }

    public List<Employee> searchEmployeesByName(String searchString) {
        return getEmployees().stream()
                .filter(it -> it.getName().toLowerCase().contains(searchString.toLowerCase()))
                .toList();
    }

    public Employee getEmployeeById(String id) {
        return Employee.from(employeeServerService.getEmployeeById(id).data());
    }

    public Integer getHighestSalary() {
        return getEmployees().stream().mapToInt(Employee::getSalary).max().orElse(0);
    }

    public List<String> topTenHighestEarningEmployeeNames() {
        return getEmployees().stream()
                .sorted(Comparator.comparing(Employee::getSalary).reversed())
                .limit(10)
                .map(Employee::getName)
                .toList();
    }

    public Boolean deleteEmployee(String name) {
        EmployeeServerResponse<Boolean> employeeServerResponse =
                employeeServerService.deleteEmployee(new EmployeeDeleteRequest(name));

        return employeeServerResponse.data();
    }

    public Employee createEmployee(EmployeeCreationRequest employeeCreationRequest) {
        return Employee.from(
                employeeServerService.createEmployee(employeeCreationRequest).data());
    }
}
