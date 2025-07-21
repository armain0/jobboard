package com.example.jobboard.services.impl;

import com.example.jobboard.domain.Role;
import com.example.jobboard.domain.dto.ApplicantDto;
import com.example.jobboard.domain.entities.ApplicantEntity;
import com.example.jobboard.exceptions.UsernameAlreadyExistsException;
import com.example.jobboard.repositories.ApplicantRepository;
import com.example.jobboard.repositories.CompanyRepository;
import com.example.jobboard.repositories.EmployerRepository;
import com.example.jobboard.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class UserServiceImplIntegrationTests {

    @Autowired
    private ApplicantRepository applicantRepository;

    @Autowired
    private EmployerRepository employerRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private ApplicantDto applicantDto;

    @BeforeEach
    void setUp() {
        applicantRepository.deleteAll();
        employerRepository.deleteAll();
        companyRepository.deleteAll();

        applicantDto = ApplicantDto.builder()
                .name("Tes Tin")
                .username("tester")
                .password("password")
                .age(25)
                .resume("3 yoe in Java")
                .build();
    }

    @Test
    @DisplayName("Test that an applicant is stored and then recalled.")
    public void testThatApplicantCanBeStoredAndRecalled() {
        ApplicantDto registeredApplicantDto = userService.registerApplicant(applicantDto);

        Optional<ApplicantEntity> applicant = applicantRepository.findByUsername("tester");

        assertThat(registeredApplicantDto).isNotNull();
        assertThat(registeredApplicantDto.getName()).isEqualTo("Tes Tin");

        assertThat(registeredApplicantDto.getPassword()).isNull();

        assertThat(applicant).isPresent();
        assertThat(applicant.get().getRole()).isEqualTo(Role.APPLICANT);
        assertThat(passwordEncoder.matches("password", applicant.get().getPassword())).isTrue();
    }

    @Test
    @DisplayName("Test that UsernameAlreadyExistsException is thrown when username already exists.")
    public void testThatUsernameAlreadyExistsExceptionIsThrown() {
        userService.registerApplicant(applicantDto);

        ApplicantDto duplicateApplicantDto = ApplicantDto.builder()
                .name("Tes Tin")
                .username("tester")
                .password("password2")
                .age(22)
                .resume("2 yoe in Python")
                .build();

        assertThrows(UsernameAlreadyExistsException.class, () -> {
            userService.registerApplicant(duplicateApplicantDto);
        }, "Expected UsernameAlreadyExistsException to be thrown for duplicate username");

        assertThat(applicantRepository.findAll()).hasSize(1);
    }

}