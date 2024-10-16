package com.example.taskday;

import com.example.taskday.domain.company.Company;
import com.example.taskday.domain.company.CompanyCreateRequestDTO;
import com.example.taskday.domain.employee.*;
import com.example.taskday.domain.employeeJobVacancy.EmployeeJobVacancyResponseDTO;
import com.example.taskday.domain.exceptions.OperationException;
import com.example.taskday.domain.jobVacancy.*;
import com.example.taskday.infra.security.SecurityFilter;
import com.example.taskday.infra.security.SecutiryConfigurations;
import com.example.taskday.infra.security.TokensService;
import com.example.taskday.repositories.CompanyRepository;
import com.example.taskday.repositories.EmployeeJobVacancyRepository;
import com.example.taskday.repositories.EmployeeRepository;
import com.example.taskday.repositories.JobVacancyRepository;
import com.example.taskday.services.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@Transactional
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest
@ContextConfiguration
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
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

    @Autowired
    private CompanyService companyService;

    @Mock
    private SecutiryConfigurations secutiryConfigurations; // ou o nome da sua classe de filtro

    @Autowired
    private CompanyRepository companyRepository;


    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private JobVacancyService jobVacancyService;

    @Mock
    private EmployeeJobVacancyService employeeJobVacancyService;

    @Autowired
    private EmployeeJobVacancyRepository employeeJobVacancyRepository;

    @Autowired
    private AuthenticationManager authenticationManager; // Moca o gerenciador de autenticação

    @Mock
    private SecurityFilter securityFilter;



    @Autowired
    private ObjectMapper objectMapper;


    private static Company company;
    private static Employee employee;

    private JobVacancy jobVacancy;
    @Autowired
    private TokensService tokensService;

    private String loginTokenCompany;

    private static String loginTokenEmployee;


    private TestUtils testUtils = new TestUtils();

    @AfterAll
    public void after(){
        employeeJobVacancyRepository.deleteAll();
        jobVacancyRepository.deleteAll();
        companyRepository.deleteAll();
        employeeRepository.deleteAll();
    }

    @BeforeAll
    public void setUp() throws OperationException {
        objectMapper.findAndRegisterModules();
        CompanyCreateRequestDTO companyCreateRequestDTO = testUtils.createCompany();
        String encryptedPassword = new BCryptPasswordEncoder().encode(companyCreateRequestDTO.password());
        companyService.createCompany(companyCreateRequestDTO, encryptedPassword);
        company = (Company) companyRepository.findByEmail("company@example.com");
        companyService.confirmationAccount(company, company.getConfirmationCode());

        EmployeeCreateRequestDTO employeeCreateRequestDTO = testUtils.createEmployee();
        String encryptedPassword1 = new BCryptPasswordEncoder().encode(employeeCreateRequestDTO.password());
        employeeService.createEmployee(employeeCreateRequestDTO, encryptedPassword1);
        employee = (Employee) employeeRepository.findByEmail("saturnino.g@estudantes.ifg.edu.br");
        employeeService.confirmationAccount(employee, employee.getConfirmationCode());


        JobVacancyCreateRequestDTO jobVacancyCreateRequestDTO = testUtils.createJobVacancy();
        jobVacancyService.createJobVacancy(jobVacancyCreateRequestDTO, company);

        loginTokenEmployee = tokensService.generateEmployeeToken(employee);
        loginTokenCompany = tokensService.generateCompanyToken(company);

    }
    @BeforeEach
    public void before(){

    }

    @AfterEach
    public void afterEach() throws OperationException {
        employeeJobVacancyRepository.deleteAll();
        jobVacancyRepository.deleteAll();
        JobVacancyCreateRequestDTO jobVacancyCreateRequestDTO = testUtils.createJobVacancy();
        jobVacancyService.createJobVacancy(jobVacancyCreateRequestDTO, company);
    }


    @Test
    @Order(1)
    public void testSubscribeToJob() throws Exception {
        List<JobVacancyResponseDTO> listJobVacancy = listAllJobVacancy(company.getName());

        assertNotNull(listJobVacancy, "A lista de vagas não deveria ser nula");
        assertFalse(listJobVacancy.isEmpty());

        JobVacancySubscribeRequestDTO jobVacancySubscribeRequestDTO = new JobVacancySubscribeRequestDTO(
                listJobVacancy.get(0).uuid().get(),
                employee.getId()
        );


        subscribeJob(jobVacancySubscribeRequestDTO);
        assertThat(employeeJobVacancyRepository.findAll()).hasSize(1);
    }


    @Test
    @Order(2)
    public void testChangeStatus() throws Exception {
        List<JobVacancyResponseDTO> listJobVacancy = listAllJobVacancy(company.getName());

        changeJobVacancy(listJobVacancy.get(0).uuid().get());

        JobVacancySubscribeRequestDTO jobVacancySubscribeRequestDTO = new JobVacancySubscribeRequestDTO(
                listJobVacancy.get(0).uuid().get(),
                employee.getId()
        );

        MvcResult result = mockMvc.perform(post("/employees/jobs/subscribe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + loginTokenEmployee)
                        .content(objectMapper.writeValueAsString(jobVacancySubscribeRequestDTO)))
                .andExpect(status().isBadRequest())
                .andReturn();

        String errorMessage = result.getResponse().getContentAsString();

        assertTrue(errorMessage.contains("Vaga inativa!"), "A mensagem de erro esperada não foi retornada.");
    }



    @Test
    public void getMinPoints() throws Exception {
        List<JobVacancyResponseDTO> listJobVacancy = listAllJobVacancy(company.getName());
        JobVacancySubscribeRequestDTO jobVacancySubscribeRequestDTO = new JobVacancySubscribeRequestDTO(
                listJobVacancy.get(0).uuid().get(),
                employee.getId()
        );

        subscribeJob(jobVacancySubscribeRequestDTO);

       List<EmployeeJobVacancyResponseDTO> employeeResponseDTO = getEmployeePPoints(listJobVacancy.get(0).uuid().get(), 700);
       assertTrue(employeeResponseDTO.isEmpty());
    }


    public List<EmployeeJobVacancyResponseDTO> getAllEmployeePerJob(UUID uuid) throws Exception {
        MvcResult result = mockMvc.perform(get("http://localhost:8080/job-vacancies/{jobVacancyId}/employees", uuid)
                      .header("Authorization", "Bearer " + loginTokenCompany)
               )
               .andExpect(status().isOk())
               .andReturn();

        String response = result.getResponse().getContentAsString();
        return objectMapper.readValue(response, new TypeReference<List<EmployeeJobVacancyResponseDTO>>() {});
    }


    public List<EmployeeJobVacancyResponseDTO> getEmployeePPoints(UUID uuid, double minPoints) throws Exception {
        MvcResult result = mockMvc.perform(get("http://localhost:8080/job-vacancies/{jobVacancyId}/employees/points", uuid)
                        .param("minPoints", String.valueOf(minPoints))
                        .header("Authorization", "Bearer " + loginTokenCompany)
                )
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        return objectMapper.readValue(response, new TypeReference<List<EmployeeJobVacancyResponseDTO>>() {});
    }


    public void subscribeJob(JobVacancySubscribeRequestDTO jobVacancySubscribeRequestDTO) throws Exception {
        objectMapper.findAndRegisterModules();
        mockMvc.perform(post("http://localhost:8080/employees/jobs/subscribe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(jobVacancySubscribeRequestDTO))
                        .header("Authorization", "Bearer " + loginTokenEmployee))
                .andExpect(status().isOk());
    }

    public void changeJobVacancy(UUID uuid) throws Exception {
        objectMapper.findAndRegisterModules();
        mockMvc.perform(put("/job-vacancies/{jobVacancyId}", uuid)
                        .contentType(MediaType.APPLICATION_JSON)  // Definindo o Content-Type como JSON
                        .header("Authorization", "Bearer " + loginTokenCompany)
                        .content("{\"status\": \"INACTIVE\"}")  // Corpo da requisição como JSON
                )
                .andExpect(status().isOk())
                .andReturn();
    }


    public List<JobVacancyResponseDTO> listAllJobVacancy(String name) throws Exception {
        objectMapper.findAndRegisterModules();
        objectMapper.registerModule(new JavaTimeModule()); // Exemplo de registro de módulo
        MvcResult result = mockMvc.perform(get("http://localhost:8080/job-vacancies").param("companyName",name))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        return objectMapper.readValue(response, new TypeReference<List<JobVacancyResponseDTO>>() {});
    }

}