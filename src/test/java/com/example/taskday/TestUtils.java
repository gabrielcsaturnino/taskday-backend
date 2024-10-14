package com.example.taskday;

import com.example.taskday.domain.company.Company;
import com.example.taskday.domain.company.CompanyCreateRequestDTO;
import com.example.taskday.domain.employee.EmployeeCreateRequestDTO;
import com.example.taskday.domain.exceptions.OperationException;
import com.example.taskday.domain.jobVacancy.JobVacancyCreateRequestDTO;
import com.example.taskday.enums.Status;
import com.example.taskday.repositories.CompanyRepository;
import com.example.taskday.services.CompanyService;
import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;


public class TestUtils {


    public static CompanyService companyService = new CompanyService();

    public  CompanyCreateRequestDTO createCompany(){
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

    public EmployeeCreateRequestDTO createEmployee(){
        List<String> experiences = Arrays.asList("Java", "Spring", "REST APIs");
        LocalDate localDate = LocalDate.parse("2001-02-20");
        return new EmployeeCreateRequestDTO(
                "John",
                "Doe",
                "saturnino.g@estudantes.ifg.edu.br",
                "123456789101112",
                "passw5dwdwddw",
                "51521164053",
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

    public  JobVacancyCreateRequestDTO createJobVacancy(){
        LocalDate localDate = LocalDate.parse("2024-10-20");
        return new JobVacancyCreateRequestDTO(
                40, // totalHoursJob
                "Software Engineer", // title
                "Responsible for developing software applications.", // description
                Arrays.asList("Java", "Spring", "Microservices"), // desiredExperience
                500.0, // dayValue
                localDate, // jobDate
                "São Paulo", // city
                "SP", // state
                Status.ACTIVE // status
        );
    }

}
