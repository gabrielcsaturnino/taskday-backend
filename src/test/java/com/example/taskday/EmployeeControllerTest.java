package com.example.taskday;

import com.example.taskday.domain.employee.*;
import com.example.taskday.enums.RoleType;
import com.example.taskday.infra.security.SecurityFilter;
import com.example.taskday.infra.security.SecutiryConfigurations;
import com.example.taskday.repositories.EmployeeRepository;
import com.example.taskday.services.EmailService;
import com.example.taskday.services.EmployeeJobVacancyService;
import com.example.taskday.services.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest
@ContextConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private EmailService emailService; // Mock do EmailService

    @InjectMocks
    private EmployeeService employeeService; // Mock do EmailService

    @Value("${api.security.token.secret}")
    private String tokenSecret;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmployeeJobVacancyService employeeJobVacancyService;

    @Mock
    private SecutiryConfigurations secutiryConfigurations; // ou o nome da sua classe de filtro

    @Autowired
    private EmployeeRepository employeeRepository; // Moca o repositório

    @Mock
    private AuthenticationManager authenticationManager; // Moca o gerenciador de autenticação

    @Mock
    private SecurityFilter securityFilter;

    private TestUtils testUtils = new TestUtils();


    @Autowired
    private ObjectMapper objectMapper;

    private static Employee employee; // Variável de instância para armazenar a empresa
    private static String loginTokenEmployee; // Variável estática para armazenar o token
    private static boolean isEmployeeCreated;


    @AfterAll
    public void after(){
        employeeRepository.deleteAll();
    }

    @BeforeAll
    public void setUpClass() throws Exception {
          createAndLoginEmployee();
    }

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
    }

    private void createAndLoginEmployee() throws Exception {
        EmployeeCreateRequestDTO employeeCreateRequestDTO = testUtils.createEmployee();
        String token = createEmployeeAndGetToken(employeeCreateRequestDTO);
        employee= (Employee) employeeRepository.findByEmail(employeeCreateRequestDTO.email());
        confirmEmployeeCode(token, employee.getConfirmationCode());

        // Realiza login para obter o token de autenticação
        EmployeeAuthenticationRequestDTO loginRequest = new EmployeeAuthenticationRequestDTO(
                employee.getEmail(),
                employeeCreateRequestDTO.password()
        );
        loginTokenEmployee = loginEmployee(loginRequest);
    }




    @Test
    public void testRegisterEmployee() throws Exception {
        List<Employee> employees = employeeRepository.findAll();
        assertThat(employees).hasSize(1);
        assertThat(employees.get(10).isEnabled()).isFalse();
    }

    @Test
    public void testLoginEmployee() throws Exception {
        Employee reloadedEmployee = (Employee) employeeRepository.findByEmail(employee.getEmail());
        assertThat(reloadedEmployee.getRoleType()).isEqualTo(RoleType.EMPLOYEE);
    }


    @Test
    public void testLoginEmployeeWithoutConfirmation() throws Exception {
        objectMapper.findAndRegisterModules();
        List<String> experiences = Arrays.asList("Java Developer", "Spring Boot", "REST APIs");
        LocalDate localDate = LocalDate.parse("2001-02-20");
        EmployeeCreateRequestDTO employeeCreateRequestDTO = new EmployeeCreateRequestDTO(
                "John",
                "Doe",
                "sssaturnino.g@estudantes.ifg.edu.br",
                "123456789101112",
                "passw5dwdwddw",
                "75566161006",
                experiences,
                "City",
                "State",
                "12345",
                "Street",
                "Complement",
                "123",
                "Address",
                localDate
        );

        String token = createEmployeeAndGetToken(employeeCreateRequestDTO);
        EmployeeAuthenticationRequestDTO loginRequest = new EmployeeAuthenticationRequestDTO(
                employeeCreateRequestDTO.email(),
                employeeCreateRequestDTO.password()
        );

        mockMvc.perform(post("http://localhost:8080/auth/login/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isForbidden());

        Employee employee = (Employee) employeeRepository.findByEmail(employeeCreateRequestDTO.email());
        confirmEmployeeCode(token, employee.getConfirmationCode());

        String loginToken = loginEmployee(loginRequest);
        Employee reloadedEmployee = (Employee) employeeRepository.findByEmail(employeeCreateRequestDTO.email());
        assertThat(reloadedEmployee.getRoleType()).isEqualTo(RoleType.EMPLOYEE);
        employeeRepository.delete(reloadedEmployee);
    }

    @Test
    public void testChangePassword() throws Exception {
        objectMapper.findAndRegisterModules();
        List<String> experiences = Arrays.asList("Java Developer", "Spring Boot", "REST APIs");
        LocalDate localDate = LocalDate.parse("2001-02-20");
        EmployeeCreateRequestDTO employeeCreateRequestDTO = new EmployeeCreateRequestDTO(
                "John",
                "Doe",
                "ssaturnino.g@estudantes.ifg.edu.br",
                "123456789101112",
                "passw5dwdwddw",
                "26667765064",
                experiences,
                "City",
                "State",
                "12345",
                "Street",
                "Complement",
                "123",
                "Address",
                localDate
        );

        String token = createEmployeeAndGetToken(employeeCreateRequestDTO);
        EmployeeAuthenticationRequestDTO loginRequest = new EmployeeAuthenticationRequestDTO(
                employeeCreateRequestDTO.email(),
                employeeCreateRequestDTO.password()
        );

        Employee employee = (Employee) employeeRepository.findByEmail("ssaturnino.g@estudantes.ifg.edu.br");
        confirmEmployeeCode(token, employee.getConfirmationCode());
        String newPassword = "123456789101112";

        String loginToken = loginEmployee(loginRequest);

        mockMvc.perform(post("http://localhost:8080/employees/request-password-change")
                        .header("Authorization", "Bearer " + loginToken))
                .andExpect(status().isOk());

        Employee employeeReload = (Employee) employeeRepository.findByEmail(employeeCreateRequestDTO.email());


        PasswordChangeRequestDTO passwordChangeRequestDTO = new PasswordChangeRequestDTO(
                newPassword, employeeReload.getConfirmationCode()
        );
        mockMvc.perform(put("http://localhost:8080/employees/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + loginToken)
                        .content(objectMapper.writeValueAsString(passwordChangeRequestDTO)))
                .andExpect(status().isOk());


        //BAD REQUEST - CÓDIGO INVÁLIDO
        PasswordChangeRequestDTO invalidPasswordChangeDTO = new PasswordChangeRequestDTO(
                newPassword, "111111111"
        );
        mockMvc.perform(post("http://localhost:8080/employees/request-password-change")
                        .header("Authorization", "Bearer " + loginToken))
                .andExpect(status().isOk());

        mockMvc.perform(put("http://localhost:8080/employees/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + loginToken)
                        .content(objectMapper.writeValueAsString(invalidPasswordChangeDTO)))
                .andExpect(status().isBadRequest());

        //BAD REQUEST - SENHA INVÁLIDA (UTILIZANDO SENHA ANTIGA)
        mockMvc.perform(post("http://localhost:8080/auth/login/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest());


        EmployeeAuthenticationRequestDTO loginRequest1 = new EmployeeAuthenticationRequestDTO(
                employeeCreateRequestDTO.email(),
                newPassword
        );

        loginEmployee(loginRequest1);
        employeeRepository.delete(employeeReload);
    }


    @Test
    public void changeAccount() throws Exception{
        String name = "Joao";
        EmployeeChangeAccountRequestDTO employeeChangeAccountRequestDTO = new EmployeeChangeAccountRequestDTO(
                Optional.of(name),           // firstName
                Optional.empty(),            // lastName
                Optional.empty(),            // email
                Optional.empty(),            // phoneNumber
                Optional.empty(),            // city
                Optional.empty(),            // state
                Optional.empty(),            // experienceList
                Optional.empty(),            // address
                Optional.empty(),            // addressStreet
                Optional.empty(),            // addressComplement
                Optional.empty(),            // addressNumber
                Optional.empty()             // postalCode
        );

        changeData(employeeChangeAccountRequestDTO);
    }



    public String createEmployeeAndGetToken(EmployeeCreateRequestDTO employeeCreateRequestDTO) throws Exception {
        MvcResult result = mockMvc.perform(post("http://localhost:8080/auth/register/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeCreateRequestDTO)))
                .andExpect(status().isOk())
                .andReturn();
        String token = result.getResponse().getContentAsString();
        return token;
    }

    public void confirmEmployeeCode(String token, String code) throws Exception {
        mockMvc.perform(post("http://localhost:8080/auth/confirmcode")
                        .param("code", code)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    public String loginEmployee(EmployeeAuthenticationRequestDTO employeeAuthenticationRequestDTO) throws Exception {
        MvcResult result = mockMvc.perform(post("http://localhost:8080/auth/login/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeAuthenticationRequestDTO)))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();

        EmployeeLoginResponseDTO response = objectMapper.readValue(jsonResponse, EmployeeLoginResponseDTO.class);
        return response.token();
    }

    public void changeData(EmployeeChangeAccountRequestDTO employeeChangeAccountRequestDTO) throws Exception {
        objectMapper.findAndRegisterModules();
        mockMvc.perform(put("http://localhost:8080/employees/account")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + loginTokenEmployee)
                .content(objectMapper.writeValueAsString(employeeChangeAccountRequestDTO))
        ).andExpect(status().isOk());

        MvcResult result = mockMvc.perform(get("http://localhost:8080/employees/me")
                .header("Authorization", "Bearer " + loginTokenEmployee)
        ).andExpect(status().isOk()).andReturn();
        String resultContent = result.getResponse().getContentAsString();
        EmployeeResponseDTO employeeResponseDTO = objectMapper.readValue(resultContent, EmployeeResponseDTO.class);
        assertEquals("Joao", employeeResponseDTO.firstName().get());
    }
}
