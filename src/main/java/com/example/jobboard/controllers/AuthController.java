package com.example.jobboard.controllers;

import com.example.jobboard.domain.dto.*;
import com.example.jobboard.services.AuthService;
import com.example.jobboard.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    @PostMapping(value = "/register/employer", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EmployerResponseDto> RegisterEmployer(@Valid @RequestBody EmployerDto employerDto) {
        EmployerResponseDto employerResponseDto = userService.registerEmployer(employerDto);

        return new ResponseEntity<>(employerResponseDto, HttpStatus.CREATED);
    }

    @PostMapping(value = "/register/applicant", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApplicantResponseDto> RegisterApplicant(@Valid @RequestBody ApplicantDto applicantDto) {
        ApplicantResponseDto applicantResponseDto = userService.registerApplicant(applicantDto);

        return new ResponseEntity<>(applicantResponseDto, HttpStatus.CREATED);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<LoginResponseDto> LoginUser(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        try {
            LoginResponseDto user = authService.authenticateUser(loginRequestDto);

            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (AuthenticationException e) {
            LoginResponseDto errorResponse = LoginResponseDto.builder()
                    .message("Failed to log in.")
                    .build();

            return new ResponseEntity<>(errorResponse, HttpStatus.CREATED);
        }
    }

}
