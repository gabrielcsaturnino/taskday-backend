package com.example.taskday.services;

import com.example.taskday.domain.company.Company;
import com.example.taskday.domain.employeeJobVacancy.EmployeeJobVacancy;
import com.example.taskday.domain.exceptions.OperationException;
import com.example.taskday.domain.jobVacancy.JobVacancy;
import com.example.taskday.domain.jobVacancy.JobVacancyChange;
import com.example.taskday.domain.jobVacancy.JobVacancyRequestDTO;
import com.example.taskday.domain.jobVacancy.JobVacancyResponseDTO;
import com.example.taskday.mappers.JobVacancyMapper;
import com.example.taskday.repositories.CompanyRepository;
import com.example.taskday.repositories.EmployeeJobVacancyRepository;
import com.example.taskday.repositories.JobVacancyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class JobVacancyService {

    @Autowired
    JobVacancyRepository jobVacancyRepository;

    @Autowired
    EmployeeJobVacancyRepository employeeJobVacancyRepository;

    @Autowired
    CompanyRepository companyRepository;


    public void createJobVacancy(JobVacancyRequestDTO jobVacancyRequestDTO, Company company) throws OperationException {

        if(company.getId() == null){
            throw new OperationException ("Erro interno");
        }

        JobVacancy jobVacancy = JobVacancyMapper.requestDTOToJobVacancy(jobVacancyRequestDTO, company);
        jobVacancy.setCompany(company);
        jobVacancyRepository.save(jobVacancy);
    }

    public List<JobVacancyResponseDTO> getJobVacanciesByCompany(String name) throws OperationException {
        Optional<Company> companyOptional = companyRepository.findByName(name);
        Company company = companyOptional.orElseThrow(() ->  new OperationException("Não foi encontrado nenhuma empresa com esse nome!"));
        UUID companyId = company.getId();
        List<JobVacancyResponseDTO> jobVacancyResponseDTOList =  jobVacancyRepository.findByCompany_Id(companyId).
                stream().map(JobVacancyMapper::toDTOJobVacancy).collect(Collectors.toList());
        return jobVacancyResponseDTOList;
    }


    @Transactional
    public void deleteJobVacancy(UUID jobVacancyId, Company company) throws OperationException {
        List<EmployeeJobVacancy> employeeJobVacancy = employeeJobVacancyRepository.findByJobVacancy_Id(jobVacancyId);
        JobVacancy jobVacancy = jobVacancyRepository.findById(jobVacancyId)
                .orElseThrow(() -> new OperationException("Erro ao deletar vaga"));

        if(!jobVacancy.getCompany().getId().equals(company.getId())){
            throw new OperationException("Erro ao deletar vaga!!");
        }

        company.getJobList().size(); // Isso força a inicialização da lista
        company.getJobList().removeIf(job -> job.getId().equals(jobVacancyId));
        jobVacancy.setCompany(null);
        companyRepository.save(company);
        jobVacancyRepository.delete(jobVacancy);

    }

    @Transactional
    public void changeJobVacancy(UUID uuid, JobVacancyChange jobVacancyChange, Company company) throws OperationException {
        JobVacancy jobVacancy = jobVacancyRepository.getReferenceById(uuid);
        if(!jobVacancy.getCompany().getId().equals(company.getId())){
            throw new OperationException("Erro ao modificar vaga!");
        }

        jobVacancyChange.title().ifPresent(jobVacancy :: setTitle);
        jobVacancyChange.dayValue().ifPresent(jobVacancy :: setDayValue);
        jobVacancyChange.jobDate().ifPresent(jobVacancy :: setJobDate);
        jobVacancyChange.totalHoursJob().ifPresent(jobVacancy :: setTotalHoursJob);
        jobVacancyChange.city().ifPresent(jobVacancy :: setCity);
        jobVacancyChange.state().ifPresent(jobVacancy :: setState);
        jobVacancyChange.desiredExperience().ifPresent(jobVacancy :: setDesiredExperience);
        jobVacancyChange.description().ifPresent(jobVacancy :: setDescription);
        jobVacancyChange.status().ifPresent(jobVacancy :: setStatus);

        jobVacancyRepository.save(jobVacancy);
    }

}
