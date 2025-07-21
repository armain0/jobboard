package com.example.jobboard.services.impl;

import com.example.jobboard.domain.JobStatus;
import com.example.jobboard.domain.dto.JobDto;
import com.example.jobboard.domain.entities.CompanyEntity;
import com.example.jobboard.domain.entities.EmployerEntity;
import com.example.jobboard.domain.entities.JobEntity;
import com.example.jobboard.exceptions.ResourceNotFoundException;
import com.example.jobboard.mappers.JobMapper;
import com.example.jobboard.repositories.CompanyRepository;
import com.example.jobboard.repositories.EmployerRepository;
import com.example.jobboard.repositories.JobRepository;
import com.example.jobboard.services.JobService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class JobServiceImplIntegrationTests {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobService jobService;

    @Autowired
    private JobMapper jobMapper;

    @Autowired
    private EmployerRepository employerRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @BeforeEach
    void setup() {
        jobRepository.deleteAll();
        employerRepository.deleteAll();
        companyRepository.deleteAll();
    }

    @Test
    @DisplayName("Test that returns all jobs made by employer")
    public void testThatJobsByEmployerReturn() {
        CompanyEntity company = CompanyEntity.builder()
                .name("Company 1")
                .website("company.com")
                .industry("Consulting")
                .build();

        companyRepository.save(company);

        EmployerEntity employer = EmployerEntity.builder()
                .name("Tes Tin")
                .username("tester")
                .password("password")
                .age(25)
                .company(company)
                .build();

        employerRepository.save(employer);

        JobDto job1 = jobService.saveJob("tester", "Job 1");
        JobDto job2 = jobService.saveJob("tester", "Job 2");
        JobDto job3 = jobService.saveJob("tester", "Job 3");

        List<JobEntity> jobs = jobRepository.findByEmployer(employer);

        assertThat(job1).isNotNull();
        assertThat(job2).isNotNull();
        assertThat(job3).isNotNull();

        assertThat(job3.getEmployer().getCompany().getName()).isEqualTo("Company 1");

        assertThat(jobs)
                .hasSize(3);
    }

    @Test
    @DisplayName("Test that a job is successfully saved for an existing employer.")
    public void testThatJobIsSuccessfullySaved() {
        CompanyEntity company = CompanyEntity.builder()
                .name("Company 1")
                .website("company.com")
                .industry("Consulting")
                .build();

        company = companyRepository.save(company);

        EmployerEntity employer = EmployerEntity.builder()
                .name("Tes Tin")
                .username("tester")
                .password("password")
                .age(25)
                .company(company)
                .build();

        employer = employerRepository.save(employer);

        String jobTitle = "Software Engineer";
        JobDto savedJobDto = jobService.saveJob(employer.getUsername(), jobTitle);

        assertThat(savedJobDto).isNotNull();
        assertThat(savedJobDto.getTitle()).isEqualTo(jobTitle);
        assertThat(savedJobDto.getStatus()).isEqualTo(JobStatus.OPEN);
        assertThat(savedJobDto.getEmployer().getUsername()).isEqualTo(employer.getUsername());

        JobEntity foundJob = jobRepository.findById(savedJobDto.getId()).orElse(null);
        assertThat(foundJob).isNotNull();
        assertThat(foundJob.getTitle()).isEqualTo(jobTitle);
        assertThat(foundJob.getEmployer().getUsername()).isEqualTo(employer.getUsername());
    }

    @Test
    @DisplayName("Test that ResourceNotFoundException is thrown when employer is not found.")
    public void testThatResourceNotFoundExceptionIsThrownWhenEmployerNotFound() {
        String nonExistentEmployerUsername = "nonexistent";
        String jobTitle = "QA Engineer";

        assertThrows(ResourceNotFoundException.class, () -> {
            jobService.saveJob(nonExistentEmployerUsername, jobTitle);
        }, "Expected ResourceNotFoundException to be thrown for non-existent employer.");
    }

}