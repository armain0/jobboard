package com.example.jobboard.services.impl;

import com.example.jobboard.domain.Role;
import com.example.jobboard.domain.dto.*;
import com.example.jobboard.domain.entities.ApplicantEntity;
import com.example.jobboard.domain.entities.CompanyEntity;
import com.example.jobboard.domain.entities.EmployerEntity;
import com.example.jobboard.mappers.ApplicantMapper;
import com.example.jobboard.mappers.CompanyMapper;
import com.example.jobboard.mappers.EmployerMapper;
import com.example.jobboard.repositories.ApplicantRepository;
import com.example.jobboard.repositories.CompanyRepository;
import com.example.jobboard.repositories.EmployerRepository;
import com.example.jobboard.services.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final EmployerRepository employerRepository;
    private final EmployerMapper employerMapper;

    private final ApplicantRepository applicantRepository;
    private final ApplicantMapper applicantMapper;

    private final CompanyMapper companyMapper;
    private final CompanyRepository companyRepository;

    public UserServiceImpl(EmployerRepository employerRepository, ApplicantRepository applicantRepository, EmployerMapper employerMapper, CompanyMapper companyMapper, ApplicantMapper applicantMapper, CompanyRepository companyRepository) {
        this.employerRepository = employerRepository;
        this.applicantRepository = applicantRepository;
        this.companyRepository = companyRepository;
        this.employerMapper = employerMapper;
        this.companyMapper = companyMapper;
        this.applicantMapper = applicantMapper;
    }

    @Override
    @Transactional
    public EmployerResponseDto registerEmployer(EmployerDto employerDto) {
        CompanyDto companyDto = employerDto.getCompany();

        Optional<CompanyEntity> newCompany = companyRepository.findByName(companyDto.getName());

        CompanyEntity savedCompany;

        if (newCompany.isEmpty()) {
            CompanyEntity company = companyMapper.companyDtoToCompany(companyDto);

            savedCompany = companyRepository.save(company);
        } else {
            savedCompany = newCompany.get();
        }

        EmployerEntity employerEntity = employerMapper.employerDtoToEmployer(employerDto);

        employerEntity.setCompany(savedCompany);
        employerEntity.setRole(Role.EMPLOYER);

        EmployerEntity savedEmployerEntity = employerRepository.save(employerEntity);

        EmployerResponseDto savedResponseDto = employerMapper.employerToResponse(savedEmployerEntity);

        return savedResponseDto;
    }

    @Override
    public ApplicantResponseDto registerApplicant(ApplicantDto applicantDto) {
        ApplicantEntity applicantEntity = applicantMapper.applicantDtoToApplicant(applicantDto);

        applicantEntity.setRole(Role.APPLICANT);

        ApplicantEntity savedApplicantEntity = applicantRepository.save(applicantEntity);

        ApplicantResponseDto savedResponseDto = applicantMapper.applicantToResponse(savedApplicantEntity);

        return savedResponseDto;
    }
}
