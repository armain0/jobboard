package com.example.jobboard.services.impl;

import com.example.jobboard.domain.ApplicationStatus;
import com.example.jobboard.domain.dto.ApplicationDto;
import com.example.jobboard.domain.entities.ApplicantEntity;
import com.example.jobboard.domain.entities.ApplicationEntity;
import com.example.jobboard.domain.entities.EmployerEntity;
import com.example.jobboard.domain.entities.JobEntity;
import com.example.jobboard.exceptions.ResourceNotFoundException;
import com.example.jobboard.mappers.ApplicationMapper;
import com.example.jobboard.repositories.ApplicantRepository;
import com.example.jobboard.repositories.ApplicationRepository;
import com.example.jobboard.repositories.EmployerRepository;
import com.example.jobboard.repositories.JobRepository;
import com.example.jobboard.services.ApplicationService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;

    private final JobRepository jobRepository;
    private final ApplicantRepository applicantRepository;
    private final EmployerRepository employerRepository;

    private final ApplicationMapper applicationMapper;

    public ApplicationServiceImpl(ApplicationRepository applicationRepository, JobRepository jobRepository, ApplicantRepository applicantRepository, EmployerRepository employerRepository, ApplicationMapper applicationMapper) {
        this.applicationRepository = applicationRepository;
        this.jobRepository = jobRepository;
        this.applicantRepository = applicantRepository;
        this.employerRepository = employerRepository;
        this.applicationMapper = applicationMapper;
    }

    @Override
    public ApplicationDto apply(String username, Long jobId) {
        ApplicantEntity applicant = applicantRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Applicant with username '" +
                        username + "' not found."));


        JobEntity job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job with id '" +
                        jobId + "' not found."));

        ApplicationEntity application = ApplicationEntity.builder()
                .applicant(applicant)
                .job(job)
                .applicationDate(LocalDateTime.now())
                .status(ApplicationStatus.PENDING)
                .build();

        ApplicationEntity savedApplication = applicationRepository.save(application);

        return applicationMapper.toDto(savedApplication);
    }

    @Override
    public List<ApplicationDto> getApplications(String username) {
        ApplicantEntity applicant = applicantRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Applicant with username '" +
                        username + "' not found."));

        List<ApplicationEntity> applicationEntities = applicationRepository
                .findByApplicant(applicant);

        return applicationEntities.stream()
                .map(applicationMapper::toDto)
                .toList();
    }

    @Override
    public List<ApplicationDto> getApplicationsAsEmployer(String username) {
        EmployerEntity employer = employerRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Employer with username '" +
                        username + "' not found."));

        List<JobEntity> jobList = jobRepository.findByEmployer(employer);

        if (jobList.isEmpty()) {
            return Collections.emptyList();
        }

        List<ApplicationEntity> applicationEntities = applicationRepository.findByJobIn(jobList);

        return applicationEntities.stream()
                .map(applicationMapper::toDto)
                .toList();
    }

    @Override
    public ApplicationDto finalizeApplication(Long id, String username, ApplicationStatus status) {
        if (!status.equals(ApplicationStatus.ACCEPTED) && !status.equals(ApplicationStatus.REJECTED)) {
            throw new IllegalArgumentException("Invalid application status." +
                    " Only ACCEPTED or REJECTED are allowed for finalization.");
        }

        ApplicationEntity application = applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application with id '" +
                        id + "' not found."));

        if (application.getJob().getEmployer() == null ||
                !application.getJob().getEmployer().getUsername().equals(username)) {
            throw new AccessDeniedException("User '" + username + "' is not authorized to finalize" +
                    " application with ID " + id + ".");
        }

        application.setStatus(status);

        ApplicationEntity savedApplication = applicationRepository.save(application);

        return applicationMapper.toDto(savedApplication);
    }

}
