package com.example.taskday;

import com.example.taskday.domain.employee.EmployeeCreateRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@ActiveProfiles("test")
public class EmployeeServiceTest {

    private EmployeeCreateRequestDTO employeeCreateRequestDTO;

    @BeforeEach
    public void setup() {

        // Configura o DTO do empregado
        List<String> experiences = Arrays.asList("Java Developer", "Spring Boot", "REST APIs");
        LocalDate dateOfBirthday = LocalDate.parse("2021-02-20");
        employeeCreateRequestDTO = new EmployeeCreateRequestDTO(
                "John",
                "Doe",
                "johndoe@example.com",
                "08063359127",
                "kjumhlozfv23",  // Senha
                "08063359127",   // Telefone
                experiences,     // Lista de experiências
                "Anapolis",      // Cidade
                "SP",            // Estado
                "wdaw",          // Outro campo necessário
                "Rua ABC",       // Endereço
                "123",           // Número
                "Apt 45",        // Complemento
                "awdw",          // Bairro
                dateOfBirthday        // Data de nascimento
        );

    }

}
