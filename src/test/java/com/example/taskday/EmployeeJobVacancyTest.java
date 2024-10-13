package com.example.taskday;

import com.example.taskday.domain.company.Company;
import com.example.taskday.domain.company.CompanyCreateRequestDTO;
import com.example.taskday.domain.employee.*;
import com.example.taskday.domain.exceptions.OperationException;
import com.example.taskday.domain.jobVacancy.JobVacancy;
import com.example.taskday.domain.jobVacancy.JobVacancyCreateRequestDTO;
import com.example.taskday.domain.jobVacancy.JobVacancyResponseDTO;
import com.example.taskday.domain.jobVacancy.JobVacancySubscribeRequestDTO;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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


    @Test
    @Order(1)
    public void testSubscribeToJob() throws Exception {
        List<JobVacancyResponseDTO> listJobVacancy = listAllJobVacancy(company.getName());

        assertNotNull(listJobVacancy, "A lista de vagas não deveria ser nula");
        assertFalse(listJobVacancy.isEmpty());

        JobVacancySubscribeRequestDTO jobVacancySubscribeRequestDTO = new JobVacancySubscribeRequestDTO(
                listJobVacancy.get(1).uuid().get(),
                employee.getId()
        );


        subscribeJob(jobVacancySubscribeRequestDTO);
        assertThat(employeeJobVacancyRepository.findAll()).hasSize(1);
    }






//    public List<EmployeeResponseDTO> getAllEmployeePerJob(UUID uuid) throws Exception {
//        // Fazendo a requisição GET, substituindo {jobVacancyId} pela variável 'uuid'
//        MvcResult result = mockMvc.perform(get("http://localhost:8080/{jobVacancyId}/employees", uuid)
//                        .header("Authorization", "Bearer " + loginTokenCompany)
//                )
//                .andExpect(status().isOk())
//                .andReturn();
//
//        // Obtendo a resposta como String e convertendo para uma lista de EmployeeResponseDTO
//        String response = result.getResponse().getContentAsString();
//        List<EmployeeResponseDTO> employeeResponseDTOS = Arrays.asList(objectMapper.readValue(response, EmployeeResponseDTO[].class));
//
//        return employeeResponseDTOS;
//    }


    public void subscribeJob(JobVacancySubscribeRequestDTO jobVacancySubscribeRequestDTO) throws Exception {
        objectMapper.findAndRegisterModules();
        mockMvc.perform(post("http://localhost:8080/employees/jobs/subscribe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(jobVacancySubscribeRequestDTO))
                        .header("Authorization", "Bearer " + loginTokenEmployee))
                .andExpect(status().isOk());
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