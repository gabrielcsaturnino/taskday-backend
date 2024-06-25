package com.example.taskday.controllers;

import com.example.taskday.domain.jobVacancy.JobVacancyRequestDTO;
import com.example.taskday.domain.jobVacancy.JobVacancyResponseDTO;
import com.example.taskday.services.JobVacancyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/job")
public class JobVacancyController {
    @Autowired
    private JobVacancyService jobVacancyService;

    @PostMapping("/addJobVacancy")
    public ResponseEntity add(@RequestBody @Validated JobVacancyRequestDTO jobVacancyRequestDTO) {
        jobVacancyService.createJobVacancy(jobVacancyRequestDTO);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/see")
    public ResponseEntity<List<JobVacancyResponseDTO>> seeJobVacancy() {
        List<JobVacancyResponseDTO> jobVacancyResponseDTOList = jobVacancyService.getAllJobVacancy();
        return new ResponseEntity<>(jobVacancyResponseDTOList, HttpStatus.OK);
    }
}
