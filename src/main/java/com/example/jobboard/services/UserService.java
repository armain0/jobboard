package com.example.jobboard.services;

import com.example.jobboard.domain.dto.ApplicantDto;
import com.example.jobboard.domain.dto.ApplicantResponseDto;
import com.example.jobboard.domain.dto.EmployerDto;
import com.example.jobboard.domain.dto.EmployerResponseDto;

public interface UserService {

    EmployerResponseDto registerEmployer(EmployerDto employerDto);

    ApplicantResponseDto registerApplicant(ApplicantDto applicantDto);
}
