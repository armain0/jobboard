package com.example.jobboard.controllers;

import com.example.jobboard.domain.dto.ApplicantDto;
import com.example.jobboard.domain.dto.ApplicantResponseDto;
import com.example.jobboard.domain.dto.EmployerDto;
import com.example.jobboard.domain.dto.EmployerResponseDto;
import com.example.jobboard.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
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

}
