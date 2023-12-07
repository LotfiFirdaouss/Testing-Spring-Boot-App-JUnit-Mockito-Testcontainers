package com.example.springboot.repository;

import com.example.springboot.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EmployeeRepositoryITests {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    public void setup(){
        employee = Employee.builder()
                .firstName("Firdaouss")
                .lastName("Lotfi")
                .email("flotfi.groupastek.ma")
                .build();
    }

    // JUnit test for save employee operation
    @Test
    @DisplayName("JUnit test for save employee operation")
    public void givenEmployeeObject_whenSave_thenReturnSavedEmployee() {
        // given - precondition or setup
//        Employee employee = Employee.builder()
//                .firstName("Firdaouss")
//                .lastName("Lotfi")
//                .email("flotfi.groupastek.ma")
//                .build();

        // when - action or the behaviour that we are going to test
        Employee savedEmployee = employeeRepository.save(employee);

        // then -verify the output
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isGreaterThan(0);
    }

    // JUnit test for
    @Test
    public void givenEmployeesList_whenFindAll_thenReturnEmployeesList() {
        // given - precondition or setup
        Employee employee1 = Employee.builder()
                .firstName("Firdaouss")
                .lastName("Lotfi")
                .email("flotfi.groupastek.ma")
                .build();

        Employee employee2 = Employee.builder()
                .firstName("Saad")
                .lastName("Lotfi")
                .email("Saad.groupastek.ma")
                .build();

        employeeRepository.save(employee1);
        employeeRepository.save(employee2);

        // when - action or the behaviour that we are going to test
        List<Employee> employees = employeeRepository.findAll();

        // then -verify the output
        assertThat(employees).isNotNull();
        assertThat(employees.size()).isEqualTo(2);
        assertThat(employees).contains(employee1);
        assertThat(employees).contains(employee2);
    }

    // JUnit test for get employee by id operation
    @Test
    public void givenEmployeeObject_whenFindById_thenReturnEmployee() {
        // given - precondition or setup
//        Employee employee = Employee.builder()
//                .firstName("Firdaouss")
//                .lastName("Lotfi")
//                .email("flotfi.groupastek.ma")
//                .build();
        employeeRepository.save(employee);

        // when - action or the behaviour that we are going to test
        Employee foundEmployee = employeeRepository.findById(employee.getId()).get();

        // then -verify the output
        assertThat(foundEmployee).isNotNull();
        assertThat(foundEmployee).isEqualTo(employee);
    }

    // JUnit test for get employee by email operation
    @Test
    public void givenEmployeeEmail_whenFindByEmail_thenReturnEmployee() {
        // given - precondition or setup
//        Employee employee = Employee.builder()
//                .firstName("Firdaouss")
//                .lastName("Lotfi")
//                .email("flotfi.groupastek.ma")
//                .build();
        employeeRepository.save(employee);

        // when - action or the behaviour that we are going to test
        Employee dbEmployee = employeeRepository.findByEmail("flotfi.groupastek.ma").get();

        // then -verify the output
        assertThat(dbEmployee).isNotNull();
        assertThat(dbEmployee).isEqualTo(employee);
    }

    // JUnit test for update employee operation
    @DisplayName("JUnit test for update employee operation")
    @Test
    public void givenEmployeeObject_whenUpdate_thenReturnUpdatedEmployee() {
        // given - precondition or setup
//        Employee employee = Employee.builder()
//                .firstName("Firdaouss")
//                .lastName("Lotfi")
//                .email("flotfi.groupastek.ma")
//                .build();
        employeeRepository.save(employee);

        // when - action or the behaviour that we are going to test
        Employee savedEmployee = employeeRepository.findById(employee.getId()).get();
        savedEmployee.setFirstName("fchanged");
        savedEmployee.setLastName("lchanged");
        savedEmployee.setEmail("e-changed");
        Employee updatedEmployee = employeeRepository.save(savedEmployee);

        // then -verify the output
        assertThat(updatedEmployee).isNotNull();
        assertThat(updatedEmployee.getFirstName()).isEqualTo("fchanged");
        assertThat(updatedEmployee.getLastName()).isEqualTo("lchanged");
        assertThat(updatedEmployee.getEmail()).isEqualTo("e-changed");
    }

    // JUnit test for delete employee operation
    @DisplayName("JUnit test for delete employee operation")
    @Test
    public void givenEmployeeObject_whenDelete_thenRemoveEmployee() {
        // given - precondition or setup
//        Employee employee = Employee.builder()
//                .firstName("Firdaouss")
//                .lastName("Lotfi")
//                .email("flotfi.groupastek.ma")
//                .build();
        employeeRepository.save(employee);

        // when - action or the behaviour that we are going to test
        employeeRepository.deleteById(employee.getId());
        Optional<Employee> deletedEmployee = employeeRepository.findById(employee.getId());

        // then -verify the output
        assertThat(deletedEmployee).isEmpty();
    }

    // JUnit test for custom query using JPQL with index
    @Test
    public void givenFirstNameAndLastName_whenFindByJPQL_thenReturnEmployeeObject() {
        // given - precondition or setup
//        Employee employee = Employee.builder()
//                .firstName("Firdaouss")
//                .lastName("Lotfi")
//                .email("flotfi.groupastek.ma")
//                .build();
        employeeRepository.save(employee);
        String firstName = "Firdaouss";
        String lastName = "Lotfi";

        // when - action or the behaviour that we are going to test
        Employee foundEmployee = employeeRepository.findByJPQL(firstName, lastName);

        // then -verify the output
        assertThat(foundEmployee).isNotNull();
    }

    // JUnit test for custom query using JPQL with NAMES
    @Test
    public void givenFirstNameAndLastName_whenFindByJPQLNamedParams_thenReturnEmployeeObject() {
        // given - precondition or setup
//        Employee employee = Employee.builder()
//                .firstName("Firdaouss")
//                .lastName("Lotfi")
//                .email("flotfi.groupastek.ma")
//                .build();
        employeeRepository.save(employee);
        String firstName = "Firdaouss";
        String lastName = "Lotfi";

        // when - action or the behaviour that we are going to test
        Employee foundEmployee = employeeRepository.findByJPQLNamedParams(firstName, lastName);

        // then -verify the output
        assertThat(foundEmployee).isNotNull();
    }

    // JUnit test for custom query using native SQL with index
    @DisplayName("JUnit test for custom query using native SQL with index")
    @Test
    public void givenFirstNameAndLastName_whenFindByNativeSQL_thenReturnEmployeeObject() {
        // given - precondition or setup
//        Employee employee = Employee.builder()
//                .firstName("Firdaouss")
//                .lastName("Lotfi")
//                .email("flotfi.groupastek.ma")
//                .build();
        employeeRepository.save(employee);

        // when - action or the behaviour that we are going to test
        Employee savedEmployee = employeeRepository.findByNativeSQL(employee.getFirstName(), employee.getLastName());

        // then -verify the output
        assertThat(savedEmployee).isNotNull();
    }

    // JUnit test for custom query using native SQL with named params
    @DisplayName("JUnit test for custom query using native SQL with named params")
    @Test
    public void givenFirstNameAndLastName_whenFindByNativeSQLNamedParams_thenReturnEmployeeObject() {
        // given - precondition or setup
//        Employee employee = Employee.builder()
//                .firstName("Firdaouss")
//                .lastName("Lotfi")
//                .email("flotfi.groupastek.ma")
//                .build();
        employeeRepository.save(employee);

        // when - action or the behaviour that we are going to test
        Employee savedEmployee = employeeRepository.findByNativeSQLNamedParams(employee.getFirstName(), employee.getLastName());

        // then -verify the output
        assertThat(savedEmployee).isNotNull();
    }


}
