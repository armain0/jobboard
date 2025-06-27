package com.example.jobboard.services.impl;

import com.example.jobboard.domain.dto.ApplicantDto;
import com.example.jobboard.domain.dto.ApplicantResponseDto;
import com.example.jobboard.domain.dto.EmployerDto;
import com.example.jobboard.domain.dto.EmployerResponseDto;
import com.example.jobboard.domain.entities.ApplicantEntity;
import com.example.jobboard.domain.entities.EmployerEntity;
import com.example.jobboard.mappers.ApplicantMapper;
import com.example.jobboard.mappers.EmployerMapper;
import com.example.jobboard.repositories.ApplicantRepository;
import com.example.jobboard.repositories.EmployerRepository;
import com.example.jobboard.services.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final EmployerRepository employerRepository;
    private final EmployerMapper employerMapper;

    private final ApplicantRepository applicantRepository;
    private final ApplicantMapper applicantMapper;

    public UserServiceImpl(EmployerRepository employerRepository, ApplicantRepository applicantRepository, EmployerMapper employerMapper, ApplicantMapper applicantMapper) {
        this.employerRepository = employerRepository;
        this.applicantRepository = applicantRepository;
        this.employerMapper = employerMapper;
        this.applicantMapper = applicantMapper;
    }

    @Override
    public EmployerResponseDto registerEmployer(EmployerDto employerDto) {
        EmployerEntity employerEntity = employerMapper.employerDtoToEmployer(employerDto);

        employerRepository.save(employerEntity);

        EmployerResponseDto savedResponseDto = employerMapper.employerToResponse(employerEntity);

        return savedResponseDto;
    }

    @Override
    public ApplicantResponseDto registerApplicant(ApplicantDto applicantDto) {
        ApplicantEntity applicantEntity = applicantMapper.applicantDtoToApplicant(applicantDto);

        applicantRepository.save(applicantEntity);

        ApplicantResponseDto savedResponseDto = applicantMapper.applicantToResponse(applicantEntity);

        return savedResponseDto;
    }
}
