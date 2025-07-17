package com.example.jobboard.controllers;

import com.example.jobboard.domain.dto.ApplicantDto;
import com.example.jobboard.domain.dto.LoginRequestDto;
import com.example.jobboard.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AuthControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

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
