package com.example.jobboard.controllers;

import com.example.jobboard.domain.dto.ApplicantDto;
import com.example.jobboard.domain.dto.ApplicantResponseDto;
import com.example.jobboard.domain.dto.EmployerDto;
import com.example.jobboard.domain.dto.EmployerResponseDto;
import com.example.jobboard.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register/employer")
    public ResponseEntity<EmployerResponseDto> RegisterEmployer(@RequestBody EmployerDto employerDto) {
        EmployerResponseDto employerResponseDto = userService.registerEmployer(employerDto);

        return new ResponseEntity<>(employerResponseDto, HttpStatus.CREATED);
    }

    @PostMapping("/register/applicant")
    public ResponseEntity<ApplicantResponseDto> RegisterApplicant(@RequestBody ApplicantDto applicantDto) {
        ApplicantResponseDto applicantResponseDto = userService.registerApplicant(applicantDto);

        return new ResponseEntity<>(applicantResponseDto, HttpStatus.CREATED);
    }

}
