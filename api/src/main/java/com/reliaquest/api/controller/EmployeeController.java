package com.reliaquest.api.controller;

import com.reliaquest.api.entity.Employee;
import com.reliaquest.api.request.EmployeeCreationRequest;
import com.reliaquest.api.service.EmployeeService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class EmployeeController implements IEmployeeController<Employee, EmployeeCreationRequest> {

    private final EmployeeService employeeService;

    @Override
    public ResponseEntity<List<Employee>> getAllEmployees() {
        log.info("Received request to get all employees");
        return ResponseEntity.ok(employeeService.getEmployees());
    }

    @Override
    public ResponseEntity<List<Employee>> getEmployeesByNameSearch(String searchString) {
        log.info("Received request to search employees by name " + searchString);
        List<Employee> searchResult = employeeService.searchEmployeesByName(searchString);
        return ResponseEntity.ok(searchResult);
    }

    @Override
    public ResponseEntity<Employee> getEmployeeById(String id) {
        log.info("Received request to get employee by id " + id);
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @Override
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        log.info("Received request to get highest salary");
        return ResponseEntity.ok(employeeService.getHighestSalary());
    }

    @Override
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        log.info("Received request to get top ten highest earning employee names");
        return ResponseEntity.ok(employeeService.topTenHighestEarningEmployeeNames());
    }

    @Override
    public ResponseEntity<Employee> createEmployee(@Valid EmployeeCreationRequest employeeInput) {
        log.info("Received request to create employee " + employeeInput);
        return ResponseEntity.ok(employeeService.createEmployee(employeeInput));
    }

    @Override
    public ResponseEntity<String> deleteEmployeeById(String id) {
        log.info("Received request to delete employee by id " + id);
        Employee employee = employeeService.getEmployeeById(id);
        if (employeeService.deleteEmployee(employee.getName())) {
            return ResponseEntity.ok(employee.getName());
        }
        return ResponseEntity.badRequest().body("Employee deletion failed with name " + employee.getName());
    }
}
