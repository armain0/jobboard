package com.example.jobboard.controllers;

import com.example.jobboard.domain.ApplicationStatus;
import com.example.jobboard.domain.dto.ApplicationDto;
import com.example.jobboard.domain.dto.ApplicationRequestDto;
import com.example.jobboard.domain.dto.JobDto;
import com.example.jobboard.domain.entities.ApplicantEntity;
import com.example.jobboard.domain.entities.CompanyEntity;
import com.example.jobboard.domain.entities.EmployerEntity;
import com.example.jobboard.repositories.*;
import com.example.jobboard.services.ApplicationService;
import com.example.jobboard.services.JobService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@AutoConfigureMockMvc
class ApplicationControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JobService jobService;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private ApplicantRepository applicantRepository;

    @Autowired
    private EmployerRepository employerRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ApplicationRepository applicationRepository;

    @BeforeEach
    void setup() {
        applicationRepository.deleteAll();
        jobRepository.deleteAll();
        employerRepository.deleteAll();
        companyRepository.deleteAll();
        applicantRepository.deleteAll();
    }

    private EmployerEntity setupEmployer(String username, String companyName) {
        CompanyEntity company = CompanyEntity.builder()
                .name(companyName)
                .website("company.com")
                .industry("Consulting")
                .build();

        companyRepository.save(company);

        EmployerEntity employer = EmployerEntity.builder()
                .name("Tes Tin")
                .username(username)
                .password("password")
                .age(25)
                .company(company)
                .build();

        return employerRepository.save(employer);
    }

    private ApplicantEntity setupApplicant(String username) {
        ApplicantEntity applicant = ApplicantEntity.builder()
                .name("Tes Tin")
                .password("password")
                .username(username)
                .age(25)
                .resume("3 yoe in Java")
                .build();

        return applicantRepository.save(applicant);
    }

    @Test
    @DisplayName("Test that an applicant can successfully post an application to a job.")
    void testThatApplicantCanPostApplication() throws Exception {
        EmployerEntity employer = setupEmployer("emp1", "CompA");

        JobDto job = jobService.saveJob(employer.getUsername(), "Job 1");

        ApplicantEntity applicant = setupApplicant("app1");

        ApplicationRequestDto applicationRequest = ApplicationRequestDto.builder()
                .id(job.getId())
                .build();

        mockMvc.perform(
                MockMvcRequestBuilders.post("/applications")
                        .with(SecurityMockMvcRequestPostProcessors.user(applicant.getUsername()).roles("APPLICANT"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(applicationRequest))
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").exists()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.applicant.username").value(applicant.getUsername())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.job.id").value(job.getId())
        );
    }

    @Test
    @DisplayName("Test that an employer cannot post an application.")
    void testThatEmployerCannotPostApplication() throws Exception {
        EmployerEntity employer = setupEmployer("emp1", "CompA");
        JobDto job = jobService.saveJob(employer.getUsername(), "Job 1");

        ApplicationRequestDto applicationRequest = ApplicationRequestDto.builder()
                .id(job.getId())
                .build();

        mockMvc.perform(
                MockMvcRequestBuilders.post("/applications")
                        .with(SecurityMockMvcRequestPostProcessors.user(employer.getUsername()).roles("EMPLOYER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(applicationRequest))
        ).andExpect(
                MockMvcResultMatchers.status().isForbidden()
        );
    }

    @Test
    @DisplayName("Test that an applicant can retrieve their own applications.")
    void testThatApplicantCanGetTheirApplications() throws Exception {
        EmployerEntity employer = setupEmployer("emp1", "CompA");

        JobDto job1 = jobService.saveJob(employer.getUsername(), "Job 1");
        JobDto job2 = jobService.saveJob(employer.getUsername(), "Job 2");

        ApplicantEntity applicant1 = setupApplicant("app1");
        ApplicantEntity applicant2 = setupApplicant("app2");

        applicationService.apply(applicant1.getUsername(), job1.getId());
        applicationService.apply(applicant1.getUsername(), job2.getId());

        applicationService.apply(applicant2.getUsername(), job2.getId());

        mockMvc.perform(
                MockMvcRequestBuilders.get("/applications")
                        .with(SecurityMockMvcRequestPostProcessors.user(applicant1.getUsername()).roles("APPLICANT"))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.length()").value(2)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].applicant.username").value(applicant1.getUsername())
        );
    }

    @Test
    @DisplayName("Test that an employer can retrieve applications for jobs they posted.")
    void testThatEmployerCanGetApplicationsForPostedJobs() throws Exception {
        EmployerEntity employer1 = setupEmployer("emp1", "CompA");
        EmployerEntity employer2 = setupEmployer("emp2", "CompB");

        JobDto job1 = jobService.saveJob(employer1.getUsername(), "Job 1");
        JobDto job2 = jobService.saveJob(employer1.getUsername(), "Job 2");
        JobDto job3 = jobService.saveJob(employer2.getUsername(), "Job 3");

        ApplicantEntity applicant1 = setupApplicant("app1");
        ApplicantEntity applicant2 = setupApplicant("app2");

        applicationService.apply(applicant1.getUsername(), job1.getId());
        applicationService.apply(applicant2.getUsername(), job1.getId());

        applicationService.apply(applicant1.getUsername(), job2.getId());

        applicationService.apply(applicant1.getUsername(), job3.getId());
        applicationService.apply(applicant2.getUsername(), job3.getId());

        mockMvc.perform(
                MockMvcRequestBuilders.get("/applications")
                        .with(SecurityMockMvcRequestPostProcessors.user(employer1.getUsername()).roles("EMPLOYER"))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.length()").value(3)
        );
    }


    @Test
    @DisplayName("Test that an employer can finalize an application to ACCEPTED.")
    void testThatEmployerCanFinalizeApplicationToAccepted() throws Exception {
        EmployerEntity employer = setupEmployer("emp1", "CompA");

        JobDto job = jobService.saveJob(employer.getUsername(), "Senior Java Developer");

        ApplicantEntity applicant = setupApplicant("app1");
        ApplicationDto createdApplication = applicationService.apply(applicant.getUsername(), job.getId());

        Map<String, ApplicationStatus> statusBody = new HashMap<>();
        statusBody.put("status", ApplicationStatus.ACCEPTED);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/applications/finalize/{id}", createdApplication.getId())
                        .with(SecurityMockMvcRequestPostProcessors.user(employer.getUsername()).roles("EMPLOYER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusBody))
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(createdApplication.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.status").value(ApplicationStatus.ACCEPTED.name())
        );
    }

    @Test
    @DisplayName("Test that an applicant cannot finalize an application.")
    void testApplicantCannotFinalizeApplication() throws Exception {
        EmployerEntity employer = setupEmployer("emp1", "CompA");

        JobDto job = jobService.saveJob(employer.getUsername(), "Senior Java Developer");

        ApplicantEntity applicant = setupApplicant("app1");
        ApplicationDto createdApplication = applicationService.apply(applicant.getUsername(), job.getId());

        Map<String, ApplicationStatus> statusBody = new HashMap<>();
        statusBody.put("status", ApplicationStatus.REJECTED);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/applications/finalize/{id}", createdApplication.getId())
                        .with(SecurityMockMvcRequestPostProcessors.user(applicant.getUsername()).roles("APPLICANT"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusBody))
        ).andExpect(
                MockMvcResultMatchers.status().isForbidden()
        );
    }
}