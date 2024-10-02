package com.example.taskday;

import com.example.taskday.domain.employee.*;
import com.example.taskday.domain.exceptions.OperationException;
import com.example.taskday.services.EmployeeService;
import com.example.taskday.repositories.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@ActiveProfiles("test") // Perfil de teste com H2
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeServiceTest {


    @Autowired
    private TestRestTemplate restTemplate;

    private String token;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;


    private EmployeeRegisterDTO employeeRegisterDTO;


    @BeforeEach
    public void setup()  {
    }






    @Test
    public void shouldCreateEmployee() throws OperationException {
        // Arrange: Criar o DTO do funcionário
        List<String> experiences = Arrays.asList("Java Developer", "Spring Boot", "REST APIs");
        LocalDate localDate = LocalDate.parse("2021-02-20");
        EmployeeRegisterDTO employeeRegisterDTO = new EmployeeRegisterDTO(
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
                localDate        // Data de nascimento
        );

        ResponseEntity<EmployeeRegisterDTO> registerResponse = restTemplate.postForEntity(
                "http://localhost:41169/auth/register/employee", // URL relativa
                employeeRegisterDTO,
                EmployeeRegisterDTO.class
        );


        // Verificar se a resposta foi 201 Created
        assertEquals(HttpStatus.CREATED, registerResponse.getStatusCode());

        // Act: Fazer login com o funcionário recém-criado
        ResponseEntity<EmployeeLoginResponseDTO> loginResponse = restTemplate.postForEntity(
                "http://localhost:41169/auth/login/employee",
                new EmployeeAuthenticationDTO("johndoe@example.com", "kjumhlozfv23"),
                EmployeeLoginResponseDTO.class
        );

        // Verificar se o login foi bem-sucedido
        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
        assertNotNull(loginResponse.getBody().token());  // Verificar se o token foi retornado

        // Act: Verificar se o funcionário foi salvo no banco de dados H2
        Employee employee = (Employee) employeeRepository.findByEmail("johndoe@example.com");

        // Assert: Validar os dados do funcionário
        assertNotNull(employee);  // Verifica se o funcionário existe no banco de dados
        assertEquals("johndoe@example.com", employee.getEmail());  // Verifica se o e-mail foi salvo corretamente
        assertEquals("John", employee.getFirstName());  // Verifica o primeiro nome
        assertEquals("Doe", employee.getLastName());    // Verifica o sobrenome
        assertEquals("kjumhlozfv23", employee.getPassword());  // Verifica se a senha foi salva corretamente
    }


    public void failedCreateEmployee() throws OperationException{

    }

}
