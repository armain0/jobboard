package com.example.jobboard.controllers;

import com.example.jobboard.domain.dto.ApplicantDto;
import com.example.jobboard.domain.dto.LoginRequestDto;
import com.example.jobboard.repositories.ApplicantRepository;
import com.example.jobboard.repositories.CompanyRepository;
import com.example.jobboard.repositories.EmployerRepository;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicantRepository applicantRepository;

    @Autowired
    private EmployerRepository employerRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @BeforeEach
    void setup() {
        applicantRepository.deleteAll();
        employerRepository.deleteAll();
        companyRepository.deleteAll();
    }

    @Test
    @DisplayName("Test that an applicant is registered and recalled.")
    void testThatApplicantCanBeRegisteredAndRecalled() throws Exception {
        ApplicantDto requestDto = ApplicantDto.builder()
                .name("Tes Tin")
                .password("password")
                .username("tester")
                .age(25)
                .resume("3 yoe in Java")
                .build();

        mockMvc.perform(
                MockMvcRequestBuilders.post("/register/applicant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.username").value("tester")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.password").doesNotExist()
        );
    }


    @Test
    @DisplayName("Test that user is successfully logged in.")
    void testThatUserLoginIsSuccessful() throws Exception {
        LoginRequestDto requestDto = LoginRequestDto.builder()
                .username("tester")
                .password("password")
                .build();

        ApplicantDto applicant = ApplicantDto.builder()
                .name("Tes Tin")
                .password("password")
                .username("tester")
                .age(25)
                .resume("3 yoe in Java")
                .build();

        userService.registerApplicant(applicant);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.message").value("Login successful.")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("tester")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.authorities[0].authority")
                        .value("ROLE_APPLICANT")
        );
    }

    @Test
    @DisplayName("Test that user log in failed.")
    public void testThatUserLoginIsUnsuccessful() throws Exception {
        LoginRequestDto requestDto = LoginRequestDto.builder()
                .username("tester")
                .password("password")
                .build();

        mockMvc.perform(
                MockMvcRequestBuilders.post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
        ).andExpect(
                MockMvcResultMatchers.status().isUnauthorized()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.message").value("Failed to log in.")
        );

    }

}
