package com.example.jobboard.services.impl;

import com.example.jobboard.domain.ApplicationStatus;
import com.example.jobboard.domain.JobStatus;
import com.example.jobboard.domain.Role;
import com.example.jobboard.domain.dto.ApplicationDto;
import com.example.jobboard.domain.entities.*;
import com.example.jobboard.exceptions.ResourceNotFoundException;
import com.example.jobboard.mappers.ApplicationMapper;
import com.example.jobboard.repositories.*;
import com.example.jobboard.services.ApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ApplicationServiceImplIntegrationTests {

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private ApplicantRepository applicantRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private ApplicationMapper applicationMapper;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private EmployerRepository employerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setup() {
        applicationRepository.deleteAll();
        jobRepository.deleteAll();
        employerRepository.deleteAll();
        companyRepository.deleteAll();
        applicantRepository.deleteAll();
    }

    @Test
    @DisplayName("Test that an applicant can successfully apply for a job.")
    public void testThatApplicantCanSuccessfullyApplyForJob() {
        CompanyEntity company = CompanyEntity.builder()
                .name("Tech Solutions Inc.")
                .website("techsolutions.com")
                .industry("IT")
                .build();

        company = companyRepository.save(company);

        EmployerEntity employer = EmployerEntity.builder()
                .name("Jane Employer")
                .password(passwordEncoder.encode("password"))
                .username("jane.employer")
                .age(35)
                .company(company)
                .build();

        employer = employerRepository.save(employer);

        JobEntity job = JobEntity.builder()
                .title("Senior Developer")
                .status(JobStatus.OPEN)
                .employer(employer)
                .build();

        job = jobRepository.save(job);

        ApplicantEntity applicant = ApplicantEntity.builder()
                .name("John Applicant")
                .username("john.applicant")
                .password(passwordEncoder.encode("applicantpass"))
                .role(Role.APPLICANT)
                .age(32)
                .resume("8 yoe in Java")
                .build();

        applicant = applicantRepository.save(applicant);

        ApplicationDto applicationDto = applicationService.apply(applicant.getUsername(), job.getId());

        assertThat(applicationDto).isNotNull();
        assertThat(applicationDto.getApplicant().getUsername()).isEqualTo(applicant.getUsername());
        assertThat(applicationDto.getJob().getTitle()).isEqualTo(job.getTitle());
        assertThat(applicationDto.getStatus()).isEqualTo(ApplicationStatus.PENDING);
        assertThat(applicationDto.getApplicationDate()).isNotNull();

        List<ApplicationEntity> applications = applicationRepository.findByApplicant(applicant);
        assertThat(applications).isNotNull();
        assertThat(applications).hasSize(1);
    }

    @Test
    @DisplayName("Test that ResourceNotFoundException is thrown when applicant not found during application.")
    public void testThatResourceNotFoundExceptionIsThrownForNonExistentApplicant() {
        CompanyEntity company = CompanyEntity.builder()
                .name("Tech Solutions Inc.")
                .website("techsolutions.com")
                .industry("IT")
                .build();

        company = companyRepository.save(company);

        EmployerEntity employer = EmployerEntity.builder()
                .name("Jane Employer")
                .password(passwordEncoder.encode("password"))
                .username("jane.employer")
                .age(35)
                .company(company)
                .build();

        employer = employerRepository.save(employer);

        JobEntity savedJob = jobRepository.save(
                JobEntity.builder()
                        .title("C# Developer")
                        .status(JobStatus.OPEN)
                        .employer(employer)
                        .build()
        );

        String nonExistentApplicantUsername = "nonexistent.applicant";

        assertThrows(ResourceNotFoundException.class, () -> {
            applicationService.apply(nonExistentApplicantUsername, savedJob.getId());
        }, "Expected ResourceNotFoundException for non-existent applicant.");
    }

    @Test
    @DisplayName("Test that ResourceNotFoundException is thrown when job not found during application.")
    public void testThatResourceNotFoundExceptionIsThrownForNonExistentJob() {
        ApplicantEntity applicant = ApplicantEntity.builder()
                .name("John Applicant")
                .username("john.applicant")
                .password(passwordEncoder.encode("applicantpass"))
                .role(Role.APPLICANT)
                .age(32)
                .resume("8 yoe in Java")
                .build();

        ApplicantEntity savedApplicant = applicantRepository.save(applicant);

        Long nonExistentJobId = 999L;

        assertThrows(ResourceNotFoundException.class, () -> {
            applicationService.apply(savedApplicant.getUsername(), nonExistentJobId);
        }, "Expected ResourceNotFoundException for non-existent job.");
    }
}