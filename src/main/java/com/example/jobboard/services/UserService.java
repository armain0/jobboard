package com.example.jobboard.services;

import com.example.jobboard.domain.dto.ApplicantDto;
import com.example.jobboard.domain.dto.EmployerDto;

public interface UserService {

    EmployerDto registerEmployer(EmployerDto employerDto);

    ApplicantDto registerApplicant(ApplicantDto applicantDto);
}
