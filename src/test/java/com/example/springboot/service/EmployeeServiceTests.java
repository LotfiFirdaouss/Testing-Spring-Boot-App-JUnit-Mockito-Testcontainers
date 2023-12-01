package com.example.springboot.service;

import com.example.springboot.exception.ResourceNotFoundException;
import com.example.springboot.model.Employee;
import com.example.springboot.repository.EmployeeRepository;
import com.example.springboot.service.impl.EmployeeServiceImpl;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

@ExtendWith(MockitoExtension.class) // to tell Mockito that we are using Mockito annotations to mock the dependencies
public class EmployeeServiceTests {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;
    private Employee employee2;

    @BeforeEach
    public void setup(){
        //employeeRepository = Mockito.mock(EmployeeRepository.class);
        //employeeService = new EmployeeServiceImpl(employeeRepository);
        employee = Employee.builder()
                .firstName("Firdaouss")
                .lastName("Lotfi")
                .email("flotfi.astekgroup.ma")
                .build();
        employee2 = Employee.builder()
                .firstName("Firdaouss")
                .lastName("Lotfi")
                .email("flotfi.astekgroup.ma")
                .build();
    }

    // JUnit test for saveEmployee method
    @DisplayName("JUnit test for saveEmployee method")
    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject() {
        // given - precondition or setup
            // configure stabbing for saveEmployee internal methods,
            // A stub is an object that provides predefined responses to specific calls or inputs,
            // without any verification or logic.
            given(employeeRepository.findByEmail(employee.getEmail()))
                    .willReturn(Optional.empty());
            given(employeeRepository.save(employee)).willReturn(employee);

        // when - action or the behaviour that we are going to test
        Employee savedEmployee = employeeService.saveEmployee(employee);
        System.out.println(savedEmployee);

        // then -verify the output
        assertThat(savedEmployee).isNotNull();
    }

    // JUnit test for saveEmployee method
    @DisplayName("JUnit test for saveEmployee method which throws Exception")
    @Test
    public void givenExistingEmail_whenSaveEmployee_then() {
        // given - precondition or setup
        given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.of(employee)); // employee exists already

        // when - action or the behaviour that we are going to test
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.saveEmployee(employee);
        });

        // then -verify the output
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    // JUnit test for get all employees method
    @Test
    public void givenEmployeesList_whenGetAllEmployees_thenReturnEmployeesList() {
        // given - precondition or setup
        given(employeeRepository.findAll())
                .willReturn(List.of(employee, employee2));

        // when - action or the behaviour that we are going to test
        List<Employee> employeeList = employeeService.getAllEmployees();

        // then -verify the output
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);
    }

    // JUnit test for get all employees method when no employees exist (negative scenario)
    @DisplayName("JUnit test for get all employees method (negative scenario)")
    @Test
    public void givenEmptyEmployeesList_whenGetAllEmployees_thenReturnEmptyEmployeesList() {
        // given - precondition or setup
        given(employeeRepository.findAll())
                .willReturn(Collections.emptyList());

        // when - action or the behaviour that we are going to test
        List<Employee> employeeList = employeeService.getAllEmployees();

        // then -verify the output
        assertThat(employeeList).isEmpty();
        assertThat(employeeList.size()).isEqualTo(0);
    }

    // JUnit test for getting employee by id
    @DisplayName("JUnit test for getting employee by id")
    @Test
    public void givenEmployeeId_whenFindEmployeeById_thenReturnEmployeeObject() {
        // given - precondition or setup
        given(employeeRepository.findById(1L))
                .willReturn(Optional.ofNullable(employee));

        // when - action or the behaviour that we are going to test
        Employee employeeById = employeeService.getEmployeeById(1l).get();

        // then -verify the output
        assertThat(employeeById).isNotNull();
        assertThat(employeeById).isEqualTo(employee);
    }

    // JUnit test for updateEmployee method
    @DisplayName("JUnit test for updateEmployee method")
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee() {
        // given - precondition or setup
        given(employeeRepository.save(employee)).willReturn(employee);
        employee.setFirstName("changed");

        // when - action or the behaviour that we are going to test
        Employee updatedEmployee = employeeService.updateEmployee(employee);

        // then -verify the output
        assertThat(updatedEmployee.getFirstName()).isEqualTo("changed");
    }

    // JUnit test for deleteEmployee method
    @DisplayName("JUnit test for deleteEmployee method")
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenEmployeeDeleted() {
        // given - precondition or setup
        long employeeId = 1L;
        willDoNothing().given(employeeRepository).deleteById(employeeId);

        // when - action or the behaviour that we are going to test
        employeeService.deleteEmployee(employeeId);

        // then -verify the output
        verify(employeeRepository, times(1)).deleteById(employeeId);
    }

}
