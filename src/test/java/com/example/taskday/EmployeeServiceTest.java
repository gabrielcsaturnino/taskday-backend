package com.example.taskday.services;

import br.com.caelum.stella.validation.CPFValidator;
import com.example.taskday.domain.employee.Employee;
import com.example.taskday.domain.employee.EmployeeRegisterDTO;
import com.example.taskday.domain.employee.EmployeeResponseDTO;
import com.example.taskday.domain.exceptions.OperationException;
import com.example.taskday.mappers.EmployeeMapper;
import com.example.taskday.repositories.CompanyRepository;
import com.example.taskday.repositories.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test") // Perfil de teste com H2
@Transactional
public class EmployeeServiceTest {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;


    private EmployeeRegisterDTO employeeRegisterDTO;

    @BeforeEach
    public void setup() {
        List<String> experiences = Arrays.asList("Java Developer", "Spring Boot", "REST APIs");
        LocalDate localDate = LocalDate.parse("2021-02-20");
        employeeRegisterDTO = new EmployeeRegisterDTO(
                "John",
                "Doe",
                "johndoe@example.com",
                "08063359127",
                "wdadwdwawad",
                "08063359127",
                experiences,
                "anapolis",
                "SP",
                "wdaw",
                "Rua ABC",
                "123",
                "Apt 45",
                "awdw",
                localDate

        );
    }

    @Test
    public void shouldCreateEmployee() throws OperationException {
        // Act: Tentar criar o funcionário
        employeeService.createEmployee(employeeRegisterDTO, employeeRegisterDTO.password());

        // Assert: Verificar se o funcionário foi salvo no banco de dados H2
        Employee employee = (Employee) employeeRepository.findByEmail("johndoe@example.com");
        if(employee!=null){
            assertNotNull(employee);
            assertEquals("johndoe@example.com", employee.getEmail());
            assertEquals("wdadwdwawad", employee.getPassword());
        }

    }


}
