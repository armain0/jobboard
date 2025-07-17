package com.example.jobboard.controllers;

import com.example.jobboard.domain.dto.ApplicantDto;
import com.example.jobboard.domain.dto.EmployerDto;
import com.example.jobboard.domain.dto.LoginRequestDto;
import com.example.jobboard.domain.dto.LoginResponseDto;
import com.example.jobboard.services.AuthService;
import com.example.jobboard.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    public AuthController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping(value = "/register/employer")
    public ResponseEntity<EmployerDto> registerEmployer(@Valid @RequestBody EmployerDto employerDto) {
        EmployerDto employerResponseDto = userService.registerEmployer(employerDto);

        return new ResponseEntity<>(employerResponseDto, HttpStatus.CREATED);
    }

    @PostMapping(value = "/register/applicant")
    public ResponseEntity<ApplicantDto> registerApplicant(@Valid @RequestBody ApplicantDto applicantDto) {
        ApplicantDto applicantResponseDto = userService.registerApplicant(applicantDto);

        return new ResponseEntity<>(applicantResponseDto, HttpStatus.CREATED);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<LoginResponseDto> loginUser(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        try {
            LoginResponseDto user = authService.authenticateUser(loginRequestDto);

            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (AuthenticationException e) {
            LoginResponseDto errorResponse = LoginResponseDto.builder()
                    .message("Failed to log in.")
                    .build();

            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }
    }

}
