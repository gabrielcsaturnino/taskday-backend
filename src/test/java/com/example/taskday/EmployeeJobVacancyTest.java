package com.example.taskday;

import com.example.taskday.domain.company.Company;
import com.example.taskday.domain.company.CompanyAuthenticationRequestDTO;
import com.example.taskday.domain.company.CompanyCreateRequestDTO;
import com.example.taskday.domain.company.CompanyLoginResponseDTO;
import com.example.taskday.domain.employee.*;
import com.example.taskday.domain.employeeJobVacancy.EmployeeJobVacancy;
import com.example.taskday.domain.jobVacancy.JobVacancy;
import com.example.taskday.domain.jobVacancy.JobVacancyCreateRequestDTO;
import com.example.taskday.domain.jobVacancy.JobVacancyResponseDTO;
import com.example.taskday.domain.jobVacancy.JobVacancySubscribeRequestDTO;
import com.example.taskday.enums.Status;
import com.example.taskday.infra.security.SecurityFilter;
import com.example.taskday.infra.security.SecutiryConfigurations;
import com.example.taskday.repositories.CompanyRepository;
import com.example.taskday.repositories.EmployeeJobVacancyRepository;
import com.example.taskday.repositories.EmployeeRepository;
import com.example.taskday.repositories.JobVacancyRepository;
import com.example.taskday.services.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.validator.internal.constraintvalidators.bv.AssertTrueValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;



import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest
@ContextConfiguration
public class EmployeeJobVacancyTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private EmailService emailService; // Mock do EmailService


    @Value("${api.security.token.secret}")
    private String tokenSecret;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JobVacancyRepository jobVacancyRepository;

    @Mock
    private CompanyService companyService;

    @Mock
    private SecutiryConfigurations secutiryConfigurations; // ou o nome da sua classe de filtro

    @Autowired
    private CompanyRepository companyRepository;


    @Autowired
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeService employeeService;

    @Mock
    private JobVacancyService jobVacancyService;

    @Mock
    private EmployeeJobVacancyService employeeJobVacancyService;

    @Autowired
    private EmployeeJobVacancyRepository employeeJobVacancyRepository;

    @Mock
    private AuthenticationManager authenticationManager; // Moca o gerenciador de autenticação

    @Mock
    private SecurityFilter securityFilter;


    @Mock
    private ObjectMapper objectMapper;

    private static Company company; // Variável de instância para armazenar a empresa
    private static String loginTokenCompany;
    private static boolean isCompanyCreated;
    private static boolean isJobCreated;

    private static Employee employee; // Variável de instância para armazenar a empresa
    private static String loginTokenEmployee;
    private static boolean isEmployeeCreated;


    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        if (!isCompanyCreated) {
            // Cria uma empresa e obtém o token para uso em testes
            CompanyCreateRequestDTO companyCreateRequestDTO = createCompanyDTO();
            String companyToken = createCompanyAndGetToken(companyCreateRequestDTO);
            company = (Company) companyRepository.findByEmail(companyCreateRequestDTO.email());
            confirmCompanyCode(companyToken, company.getConfirmationCode());

            isCompanyCreated = true; // Marca que a empresa foi criada
        }


        if (!isEmployeeCreated) {
            // Cria uma empresa e obtém o token para uso em testes
            EmployeeCreateRequestDTO employeeCreateRequestDTO = createEmployeeDTO();
            String employeeToken = createEmployeeAndGetToken(employeeCreateRequestDTO);
            employee = (Employee) employeeRepository.findByEmail(employeeCreateRequestDTO.email());
            confirmEmployeeCode(employeeToken, employee.getConfirmationCode());

            isEmployeeCreated = true; // Marca que a empresa foi criada
        }

        EmployeeAuthenticationRequestDTO employeeAuthenticationRequestDTO = new EmployeeAuthenticationRequestDTO(
                employee.getEmail(),
                "passw5dwdwddw"
        );

        loginTokenEmployee = loginEmployee(employeeAuthenticationRequestDTO);

        CompanyAuthenticationRequestDTO loginRequest = new CompanyAuthenticationRequestDTO(
                company.getEmail(),
                "123456789101112"
        );
        loginTokenCompany = loginCompany(loginRequest);

        if(!isJobCreated){
            addJobVacancy();
            isJobCreated = true;
        }


    }




        @Test
        @Order(1)
        public void testSubscribeToJob() throws Exception {
            List<JobVacancyResponseDTO> listJobVacancy = listAllJobVacancy("NOME1");
            JobVacancySubscribeRequestDTO jobVacancySubscribeRequestDTO = new JobVacancySubscribeRequestDTO(
                    listJobVacancy.get(0).uuid().get(),
                    employee.getId()
            );
            subscribeJob(jobVacancySubscribeRequestDTO);
            assertThat(employeeJobVacancyRepository.findAll()).hasSize(1);
        }

        @Test
        @Order(2)
        public void testGetEmployees() throws Exception {
            List<JobVacancyResponseDTO> listJobVacancy = listAllJobVacancy("NOME1");
            List<EmployeeResponseDTO> employeeResponseDTOS = getAllEmployeePerJob(listJobVacancy.get(0).uuid().get());
            assertThat(employeeResponseDTOS).isNotEmpty();
    }




    private CompanyCreateRequestDTO createCompanyDTO() {
        objectMapper.findAndRegisterModules();
        return new CompanyCreateRequestDTO(
                "NOME1",
                "06.947.284/0001-04",
                "wd",
                "wd",
                "wd",
                "wd",
                "dw",
                "wdwd",
                "22222",
                "123456789101112",
                "company@example.com",
                "dw",
                "dw",
                "08063359127"
        );
    }

    public String createCompanyAndGetToken(CompanyCreateRequestDTO companyCreateRequestDTO) throws Exception {
        MvcResult result = mockMvc.perform(post("http://localhost:8080/auth/register/company")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(companyCreateRequestDTO)))
                .andExpect(status().isOk())
                .andReturn();
        return result.getResponse().getContentAsString();
    }

    public void confirmCompanyCode(String token, String code) throws Exception {
        mockMvc.perform(post("http://localhost:8080/auth/confirmcode")
                        .param("code", code)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    public String loginCompany(CompanyAuthenticationRequestDTO companyAuthenticationRequestDTO) throws Exception {
        MvcResult result = mockMvc.perform(post("http://localhost:8080/auth/login/company")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(companyAuthenticationRequestDTO)))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        CompanyLoginResponseDTO response = objectMapper.readValue(jsonResponse, CompanyLoginResponseDTO.class);
        return response.token();
    }


    private EmployeeCreateRequestDTO createEmployeeDTO(){
        objectMapper.findAndRegisterModules();
        List<String> experiences = Arrays.asList("Java Developer", "Spring Boot", "REST APIs");
        LocalDate localDate = LocalDate.parse("2001-02-20");
        return new EmployeeCreateRequestDTO(
                "John",
                "Doe",
                "saturnino.g@estudantes.ifg.edu.br",
                "123456789101112",
                "passw5dwdwddw",
                "48688801010",
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


    public void addJobVacancy() throws Exception {
        objectMapper.findAndRegisterModules();
        JobVacancyCreateRequestDTO jobVacancyCreateRequestDTO = new JobVacancyCreateRequestDTO(
                40, // totalHoursJob
                "Software Engineer", // title
                "Responsible for developing software applications.", // description
                Arrays.asList("Java", "Spring", "Microservices"), // desiredExperience
                500.0, // dayValue
                LocalDate.now().plusDays(5), // jobDate
                "São Paulo", // city
                "SP", // state
                Status.ACTIVE // status
        );

        mockMvc.perform(post("http://localhost:8080/job-vacancies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + loginTokenCompany)
                        .content(objectMapper.writeValueAsString(jobVacancyCreateRequestDTO)))
                .andExpect(status().isCreated());

        assertThat(jobVacancyRepository.findAll()).hasSize(1);

    }

    public List<EmployeeResponseDTO> getAllEmployeePerJob(UUID uuid) throws Exception {
        // Fazendo a requisição GET, substituindo {jobVacancyId} pela variável 'uuid'
        MvcResult result = mockMvc.perform(get("http://localhost:8080/{jobVacancyId}/employees", uuid)
                        .header("Authorization", "Bearer " + loginTokenCompany)
                )
                .andExpect(status().isOk())
                .andReturn();

        // Obtendo a resposta como String e convertendo para uma lista de EmployeeResponseDTO
        String response = result.getResponse().getContentAsString();
        List<EmployeeResponseDTO> employeeResponseDTOS = Arrays.asList(objectMapper.readValue(response, EmployeeResponseDTO[].class));

        return employeeResponseDTOS;
    }


    public void subscribeJob(JobVacancySubscribeRequestDTO jobVacancySubscribeRequestDTO) throws Exception {
        mockMvc.perform(post("http://localhost:8080/employees/jobs/subscribe")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(jobVacancySubscribeRequestDTO))
                        .header("Authorization", "Bearer " + loginTokenEmployee))
                .andExpect(status().isOk());
    }


    public List<JobVacancyResponseDTO> listAllJobVacancy(String name) throws Exception {
        objectMapper.findAndRegisterModules();
        MvcResult result = mockMvc.perform(get("http://localhost:8080/job-vacancies").param("companyName",name))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        List<JobVacancyResponseDTO> listJobVacancy = (objectMapper.readValue(response,
                new TypeReference <List<JobVacancyResponseDTO>>() {}));
        return listJobVacancy;
    }



}
