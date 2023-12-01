package com.example.springboot.service;

import com.example.springboot.model.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    public Employee saveEmployee(Employee employee);
    List<Employee> getAllEmployees();
    Optional<Employee> getEmployeeById(Long id);
    Employee updateEmployee(Employee updatedEmployee);
    void deleteEmployee(long id);
}
