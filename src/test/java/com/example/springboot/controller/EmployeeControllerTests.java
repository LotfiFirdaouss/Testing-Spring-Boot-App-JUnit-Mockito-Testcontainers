package com.example.springboot.controller;

import com.example.springboot.model.Employee;
import com.example.springboot.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.hamcrest.CoreMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest
public class EmployeeControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    private Employee employee;

    @BeforeEach
    public void setup(){
        //employeeRepository = Mockito.mock(EmployeeRepository.class);
        //employeeService = new EmployeeServiceImpl(employeeRepository);
        employee = Employee.builder()
                .firstName("Firdaouss")
                .lastName("Lotfi")
                .email("flotfi.astekgroup.ma")
                .build();
    }


    // JUnit test for CreateEmployee Rest API
    @DisplayName("JUnit test for CreateEmployee Rest API")
    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
        // given - precondition or setup
        given(employeeService.saveEmployee(any(Employee.class)))
                .willAnswer((invocation -> invocation.getArgument(0)));

        // when - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        // then -verify the output
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));

    }

    // JUnit test for GetAllEmployees Rest API
    @DisplayName("JUnit test for GetAllEmployees Rest API")
    @Test
    public void givenEmployeesList_whenGetAllEmployess_thenReturnEmployeesList() throws Exception {
        // given - precondition or setup
        given(employeeService.getAllEmployees())
                .willReturn(List.of(employee));

        // when - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(get("/api/employees"));

        // then -verify the output
        response
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(1)))
                .andExpect(jsonPath("$[0].firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$[0].lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$[0].email", is(employee.getEmail())));
    }

    // POSITIVE scenario - valid employee id
    // JUnit test for GetEmployeeById Rest API
    @Test
    @DisplayName("JUnit test for GetEmployeeById Rest API")
    public void givenEmployeeId_whenGetEmployeeById_thenReurnEmployeeObject() throws Exception {
        // given - precondition or setup
        long employeeId = 1L;
        given(employeeService.getEmployeeById(employeeId))
                .willReturn(Optional.of(employee));

        // when - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));

        // then -verify the output
        response
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));
    }

    // NAGATIVE scenario - valid employee id
    // JUnit test for GetEmployeeById Rest API
    @Test
    @DisplayName("JUnit test for GetEmployeeById Rest API -- NEGATIVE SCENARIO")
    public void givenInvalidEmployeeId_whenGetEmployeeById_thenReturnEmpty() throws Exception {
        // given - precondition or setup
        long employeeId = 1L;
        given(employeeService.getEmployeeById(employeeId))
                .willReturn(Optional.empty());

        // when - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));

        // then -verify the output
        response
                .andExpect(status().isNotFound());
    }

    // POSITIVE scenario
    // JUnit test for UpdateEmployee Rest API
    @DisplayName("JUnit test for UpdateEmployee Rest API")
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee() throws Exception {
        // given - precondition or setup
        long employeeId = 1L;
        Employee newEmployee = employee;
        newEmployee.setFirstName("changed");
        newEmployee.setLastName("Lchanged");
        newEmployee.setEmail("Echanged");
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(employee));
        given(employeeService.updateEmployee(any(Employee.class)))
                .willAnswer((invocation -> invocation.getArgument(0)));

        // when - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newEmployee)));

        // then -verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(newEmployee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(newEmployee.getLastName())))
                .andExpect(jsonPath("$.email", is(newEmployee.getEmail())));
    }

    // NEGATIVE scenario
    // JUnit test for UpdateEmployee Rest API
    @DisplayName("JUnit test for UpdateEmployee Rest API -- NEGATIVE SCENARIO")
    @Test
    public void givenInvalidEmployeeId_whenUpdateEmployee_thenReturnNotFound() throws Exception {
        // given - precondition or setup
        long employeeId = 1L;
        Employee newEmployee = employee;
        newEmployee.setFirstName("changed");
        newEmployee.setLastName("Lchanged");
        newEmployee.setEmail("Echanged");
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());
        given(employeeService.updateEmployee(any(Employee.class)))
                .willAnswer((invocation -> invocation.getArgument(0)));

        // when - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newEmployee)));

        // then -verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    // POSITIVE scenario
    // JUnit test for deleteEmployee Rest API
    @DisplayName("JUnit test for deleteEmployee Rest API")
    @Test
    public void givenValidEmployeeId_whenDeleteEmployee_thenReturn200Status() throws Exception {
        // given - precondition or setup
        long employeeId = 1L;
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(employee));
        willDoNothing().given(employeeService).deleteEmployee(employeeId);

        // when - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(delete("/api/employees/{id}", employeeId));

        // then -verify the output
        response.andExpect(status().isOk())
                .andDo(print());
    }

    // NEGATIVE scenario
    // JUnit test for deleteEmployee Rest API
    @DisplayName("JUnit test for deleteEmployee Rest API -- NEGATIVE scenario")
    @Test
    public void givenInvalidEmployeeId_whenDeleteEmployee_thenReturn404Status() throws Exception {
        // given - precondition or setup
        long employeeId = 1L;
        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());
        willDoNothing().given(employeeService).deleteEmployee(employeeId);

        // when - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(delete("/api/employees/{id}", employeeId));

        // then -verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());
    }
}
