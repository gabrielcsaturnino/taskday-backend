package com.example.taskday.repositories;

import com.example.taskday.domain.employee.Employee;
import com.example.taskday.domain.employee.EmployeeRegisterDTO;
import com.example.taskday.services.EmployeeService;
import org.h2.result.Row;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.h2.*;
import org.h2.tools.SimpleResultSet;
import org.springframework.test.context.ActiveProfiles;

import java.sql.SQLException;
import java.sql.Types;

@DataJpaTest
@ActiveProfiles("test")
class EmployeeJobVacancyRepositoryTest {

    @Test
    void findByEmployee_Id() throws SQLException {

    }

}