package com.example.springboot.integration;

import com.example.springboot.model.Employee;
import com.example.springboot.repository.EmployeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmployeeControllerITests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Employee employee;

    @BeforeEach
    void setup(){
        employeeRepository.deleteAll();
        employee = Employee.builder()
                .firstName("Firdaouss")
                .lastName("Lotfi")
                .email("flotfi.astekgroup.ma")
                .build();
    }

    // Integration test for CreateEmployee Rest API
    @DisplayName("Integration test for CreateEmployee Rest API")
    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
        // given - precondition or setup
        // create employee instance (in setup)

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

    // Integration test for GetAllEmployees Rest API
    @DisplayName("Integration test for GetAllEmployees Rest API")
    @Test
    public void givenEmployeesList_whenGetAllEmployess_thenReturnEmployeesList() throws Exception {
        // given - precondition or setup
        // create employee instance (in setup)
        employeeRepository.saveAll(List.of(employee));

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

    // POSITIVE scenario
    // Integration test for UpdateEmployee Rest API
    @DisplayName("Integration test for UpdateEmployee Rest API")
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee() throws Exception {
        // given - precondition or setup
        employeeRepository.save(employee);
        // the new updated object
        Employee newEmployee = Employee.builder()
                .firstName("changed")
                .lastName("Lchanged")
                .email("Echanged.astekgroup.ma")
                .build();

        // when - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employee.getId())
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
    // Integration test for UpdateEmployee Rest API
    @DisplayName("Integration test for UpdateEmployee Rest API -- NEGATIVE SCENARIO")
    @Test
    public void givenInvalidEmployeeId_whenUpdateEmployee_thenReturnNotFound() throws Exception {
        // given - precondition or setup
        long employeeId = 1L;
        // the new updated object
        Employee newEmployee = Employee.builder()
                .firstName("changed")
                .lastName("Lchanged")
                .email("Echanged.astekgroup.ma")
                .build();


        // when - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newEmployee)));

        // then -verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    // POSITIVE scenario
    // Integration test for deleteEmployee Rest API
    @DisplayName("Integration test for deleteEmployee Rest API")
    @Test
    public void givenValidEmployeeId_whenDeleteEmployee_thenReturn200Status() throws Exception {
        // given - precondition or setup
        Employee savedEmployee = employeeRepository.save(employee);

        // when - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(delete("/api/employees/{id}", savedEmployee.getId()));

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

        // when - action or the behaviour that we are going to test
        ResultActions response = mockMvc.perform(delete("/api/employees/{id}", employeeId));

        // then -verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

}
