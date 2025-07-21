package com.example.jobboard.controllers;

import com.example.jobboard.domain.JobStatus;
import com.example.jobboard.domain.dto.JobDto;
import com.example.jobboard.domain.entities.ApplicantEntity;
import com.example.jobboard.domain.entities.CompanyEntity;
import com.example.jobboard.domain.entities.EmployerEntity;
import com.example.jobboard.repositories.ApplicantRepository;
import com.example.jobboard.repositories.CompanyRepository;
import com.example.jobboard.repositories.EmployerRepository;
import com.example.jobboard.repositories.JobRepository;
import com.example.jobboard.services.JobService;
import com.example.jobboard.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class JobControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JobService jobService;

    @Autowired
    private EmployerRepository employerRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private ApplicantRepository applicantRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private UserService userService;

    @BeforeEach
    void setup() {
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

    @Test
    @DisplayName("Test that getJobs returns UNAUTHORIZED when unauthenticated.")
    void testThatGetJobsIsUnauthorized() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/jobs")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isUnauthorized()
        );
    }

    @Test
    @DisplayName("Test that getJobs returns all jobs for ROLE_APPLICANT.")
    void testThatAllJobsReturnedAsApplicant() throws Exception {
        EmployerEntity employer1 = setupEmployer("employer1", "CompA");
        EmployerEntity employer2 = setupEmployer("employer2", "CompB");

        jobService.saveJob(employer1.getUsername(), "Job 1");
        jobService.saveJob(employer1.getUsername(), "Job 2");
        jobService.saveJob(employer2.getUsername(), "Job 3");

        ApplicantEntity applicant = ApplicantEntity.builder()
                .name("Tes Tin")
                .password("password")
                .username("tester")
                .age(25)
                .resume("3 yoe in Java")
                .build();

        mockMvc.perform(
                MockMvcRequestBuilders.get("/jobs")
                        .with(SecurityMockMvcRequestPostProcessors.user(applicant.getUsername()).roles("APPLICANT"))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.length()").value(3)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].title").exists()
        );
    }

    @Test
    @DisplayName("Test that closeJob returns UNAUTHORIZED when unauthenticated.")
    void testThatCloseJobIsUnauthenticated() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.patch("/jobs/close/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isUnauthorized()
        );
    }

    @Test
    @DisplayName("Test that closeJob successfully closes a job for the owning employer.")
    void testThatCloseJobIsSuccessful() throws Exception {
        EmployerEntity employer = setupEmployer("employer1", "CompA");
        JobDto jobToClose = jobService.saveJob(employer.getUsername(), "Job 1");

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/jobs/close/{id}", jobToClose.getId())
                        .with(SecurityMockMvcRequestPostProcessors.user(employer.getUsername()).roles("EMPLOYER"))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(jobToClose.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.status").value(JobStatus.CLOSED.name())
        );
    }

    @Test
    @DisplayName("Test closeJob returns NOT_FOUND if job does not exist.")
    void testCloseJobNotFound() throws Exception {
        EmployerEntity employer = setupEmployer("employer1", "CompA");

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/jobs/close/{id}", 999L)
                        .with(SecurityMockMvcRequestPostProcessors.user(employer.getUsername()).roles("EMPLOYER"))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    @DisplayName("Test closeJob returns FORBIDDEN if applicant tries to close a job.")
    void testCloseJobAsApplicantForbidden() throws Exception {
        EmployerEntity employer = setupEmployer("employer1", "CompA");
        JobDto jobToClose = jobService.saveJob(employer.getUsername(), "Job 1");

        ApplicantEntity applicant = applicantRepository.save(
                ApplicantEntity.builder()
                        .name("Tes Tin")
                        .password("password")
                        .username("tester")
                        .age(25)
                        .resume("3 yoe in Java")
                        .build()
        );

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/jobs/close/{id}", jobToClose.getId())
                        .with(SecurityMockMvcRequestPostProcessors.user(applicant.getUsername()).roles("APPLICANT"))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isForbidden()
        );
    }

    @Test
    @DisplayName("Test closeJob returns FORBIDDEN if employer tries to close another employer's job.")
    void testCloseJobAnotherEmployersJobForbidden() throws Exception {
        EmployerEntity employer1 = setupEmployer("employer1", "CompA");
        EmployerEntity employer2 = setupEmployer("employer2", "CompB");

        JobDto jobToClose = jobService.saveJob(employer1.getUsername(), "Job 1");

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/jobs/close/{id}", jobToClose.getId())
                        .with(SecurityMockMvcRequestPostProcessors.user(employer2.getUsername()).roles("EMPLOYER"))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isForbidden()
        );
    }

}