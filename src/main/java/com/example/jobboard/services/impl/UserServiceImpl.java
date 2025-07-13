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
import org.springframework.security.crypto.password.PasswordEncoder;
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

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(EmployerRepository employerRepository, ApplicantRepository applicantRepository, EmployerMapper employerMapper, CompanyMapper companyMapper, ApplicantMapper applicantMapper, CompanyRepository companyRepository, PasswordEncoder passwordEncoder) {
        this.employerRepository = employerRepository;
        this.applicantRepository = applicantRepository;
        this.companyRepository = companyRepository;
        this.employerMapper = employerMapper;
        this.companyMapper = companyMapper;
        this.applicantMapper = applicantMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public EmployerResponseDto registerEmployer(EmployerDto employerDto) {
        CompanyDto companyDto = employerDto.getCompany();

        Optional<CompanyEntity> newCompany = companyRepository.findByName(companyDto.getName());

        CompanyEntity savedCompany;

        if (newCompany.isEmpty()) {
            CompanyEntity company = companyMapper.toEntity(companyDto);

            savedCompany = companyRepository.save(company);
        } else {
            savedCompany = newCompany.get();
        }

        String hashedPassword = passwordEncoder.encode(employerDto.getPassword());

        EmployerEntity employerEntity = employerMapper.toEntity(employerDto);

        employerEntity.setPassword(hashedPassword);
        employerEntity.setCompany(savedCompany);
        employerEntity.setRole(Role.EMPLOYER);

        EmployerEntity savedEmployerEntity = employerRepository.save(employerEntity);

        EmployerResponseDto savedResponseDto = employerMapper.toResponseDto(savedEmployerEntity);

        return savedResponseDto;
    }

    @Override
    public ApplicantResponseDto registerApplicant(ApplicantDto applicantDto) {
        String hashedPassword = passwordEncoder.encode(applicantDto.getPassword());

        ApplicantEntity applicantEntity = applicantMapper.toEntity(applicantDto);

        applicantEntity.setPassword(hashedPassword);
        applicantEntity.setRole(Role.APPLICANT);

        ApplicantEntity savedApplicantEntity = applicantRepository.save(applicantEntity);

        ApplicantResponseDto savedResponseDto = applicantMapper.toResponseDto(savedApplicantEntity);

        return savedResponseDto;
    }
}
