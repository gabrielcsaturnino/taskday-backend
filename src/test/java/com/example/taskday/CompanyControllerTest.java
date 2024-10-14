package com.example.taskday;


import com.example.taskday.domain.company.*;
import com.example.taskday.domain.jobVacancy.JobVacancy;
import com.example.taskday.domain.jobVacancy.JobVacancyCreateRequestDTO;
import com.example.taskday.enums.RoleType;
import com.example.taskday.enums.Status;
import com.example.taskday.infra.security.SecurityFilter;
import com.example.taskday.infra.security.SecutiryConfigurations;
import com.example.taskday.repositories.CompanyRepository;
import com.example.taskday.repositories.EmployeeRepository;
import com.example.taskday.repositories.JobVacancyRepository;
import com.example.taskday.services.CompanyService;
import com.example.taskday.services.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
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
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest
@ContextConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CompanyControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Mock
    private EmailService emailService;

    @Value("${api.security.token.secret}")
    private String tokenSecret;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JobVacancyRepository jobVacancyRepository;

    @Mock
    private CompanyService companyService;

    @Mock
    private SecutiryConfigurations secutiryConfigurations;

    @Autowired
    private CompanyRepository companyRepository;
    @Mock
    private AuthenticationManager authenticationManager;

    public static JobVacancy createdJobVacancy;

    @Mock
    private SecurityFilter securityFilter;

    private TestUtils testUtils = new TestUtils();

    private  Company company;
    private  String loginTokenCompany;



    JobVacancyCreateRequestDTO jobVacancyCreateRequestDTO;

    @AfterAll
    public void after(){
        companyRepository.deleteAll();
        jobVacancyRepository.deleteAll();
    }

    @BeforeAll
    public void setUpClass() throws Exception {
        createAndLoginCompany();
        jobVacancyCreateRequestDTO = testUtils.createJobVacancy();
    }
    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void afterE(){
        jobVacancyRepository.deleteAll();
    }



    private void createAndLoginCompany() throws Exception {
        objectMapper.findAndRegisterModules();
        CompanyCreateRequestDTO companyCreateRequestDTO = testUtils.createCompany();
        String token = createCompanyAndGetToken(companyCreateRequestDTO);
        company = (Company) companyRepository.findByEmail(companyCreateRequestDTO.email());
        confirmCompanyCode(token, company.getConfirmationCode());

        CompanyAuthenticationRequestDTO loginRequest = new CompanyAuthenticationRequestDTO(
                company.getEmail(),
                companyCreateRequestDTO.password()
        );
        loginTokenCompany = loginCompany(loginRequest);
    }


    @Test
    public void deleteJob() throws Exception{

        testAddJobVacancy();
        List<JobVacancy> jobVacancies = jobVacancyRepository.findByCompany_Id(company.getId());
        UUID uuid = jobVacancies.get(0).getId();
        mockMvc.perform(delete("/job-vacancies/{jobVacancyId}", uuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + loginTokenCompany)
                        )
                .andExpect(status().isAccepted());

        List<JobVacancy> jobVacancy = jobVacancyRepository.findByCompany_Id(company.getId());
        assertThat(jobVacancy.isEmpty());
    }

    @Test
    public void testRegisterCompany() throws Exception {
        assertThat(company.getEmail()).isEqualTo("company@example.com");
        assertThat(company.getRoleType()).isEqualTo(RoleType.INACTIVE);
        assertThat(company.isEnabled()).isFalse();

        Company reloadedCompany = (Company) companyRepository.findByEmail(company.getEmail());
        assertThat(reloadedCompany.getRoleType()).isEqualTo(RoleType.COMPANY);

        List<Company> companies = companyRepository.findAll();
        assertThat(companies).hasSize(1);
        assertThat(companies.get(0).isEnabled()).isTrue();
    }

    @Test
    public void testLoginCompany() throws Exception {
        Company reloadedCompany = (Company) companyRepository.findByEmail(company.getEmail());
        assertThat(reloadedCompany.getRoleType()).isEqualTo(RoleType.COMPANY);
    }

    @Test
    public void testChangeCompanyData() throws Exception {
        String name = "New Company Name";
        CompanyChangeAccountRequestDTO companyChangeAccountRequestDTO = new CompanyChangeAccountRequestDTO(
                Optional.of(name),
                Optional.empty(), // addressStreet
                Optional.empty(), // addressComplement
                Optional.empty(), // addressNumber
                Optional.empty(), // address
                Optional.empty(), // city
                Optional.empty(), // state
                Optional.empty(), // postalCode
                Optional.empty(), // email
                Optional.empty()
        );

        changeData(companyChangeAccountRequestDTO);
    }

    @Test
    public void testAddJobVacancy() throws Exception {
        objectMapper.findAndRegisterModules();
        mockMvc.perform(post("/job-vacancies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + loginTokenCompany)
                        .content(objectMapper.writeValueAsString(jobVacancyCreateRequestDTO)))
                .andExpect(status().isCreated());

        assertThat(jobVacancyRepository.findAll()).hasSize(1);
        createdJobVacancy = jobVacancyRepository.findAll().get(0);
    }





    public  String createCompanyAndGetToken(CompanyCreateRequestDTO companyCreateRequestDTO) throws Exception {
        objectMapper.findAndRegisterModules();
        MvcResult result = mockMvc.perform(post("http://localhost:8080/auth/register/company")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(companyCreateRequestDTO)))
                .andExpect(status().isOk())
                .andReturn();
        return result.getResponse().getContentAsString();
    }

    public  void confirmCompanyCode(String token, String code) throws Exception {
        mockMvc.perform(post("http://localhost:8080/auth/confirmcode")
                        .param("code", code)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    public  String loginCompany(CompanyAuthenticationRequestDTO companyAuthenticationRequestDTO) throws Exception {
        MvcResult result = mockMvc.perform(post("http://localhost:8080/auth/login/company")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(companyAuthenticationRequestDTO)))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        CompanyLoginResponseDTO response = objectMapper.readValue(jsonResponse, CompanyLoginResponseDTO.class);
        return response.token();
    }

    public void changeData(CompanyChangeAccountRequestDTO companyChangeAccountRequestDTO) throws Exception {
        objectMapper.findAndRegisterModules();
        mockMvc.perform(MockMvcRequestBuilders.put("http://localhost:8080/companies/account")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + loginTokenCompany)
                .content(objectMapper.writeValueAsString(companyChangeAccountRequestDTO))
        ).andExpect(status().isOk());

        MvcResult result = mockMvc.perform(get("http://localhost:8080/companies/me")
                        .header("Authorization", "Bearer " + loginTokenCompany))
                .andExpect(status().isOk())
                .andReturn();

        String resultContent = result.getResponse().getContentAsString();
        CompanyResponseDTO companyResponseDTO = objectMapper.readValue(resultContent, CompanyResponseDTO.class);
        assertEquals("New Company Name", companyResponseDTO.name().get());
    }
}
