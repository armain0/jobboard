package com.example.jobboard.services.impl;

import com.example.jobboard.domain.ApplicationStatus;
import com.example.jobboard.domain.dto.ApplicationDto;
import com.example.jobboard.domain.entities.ApplicantEntity;
import com.example.jobboard.domain.entities.ApplicationEntity;
import com.example.jobboard.domain.entities.EmployerEntity;
import com.example.jobboard.domain.entities.JobEntity;
import com.example.jobboard.mappers.ApplicationMapper;
import com.example.jobboard.repositories.ApplicantRepository;
import com.example.jobboard.repositories.ApplicationRepository;
import com.example.jobboard.repositories.EmployerRepository;
import com.example.jobboard.repositories.JobRepository;
import com.example.jobboard.services.ApplicationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
        Optional<ApplicantEntity> applicant = applicantRepository.findByUsername(username);

        Optional<JobEntity> job = jobRepository.findById(jobId);

        if (applicant.isPresent() && job.isPresent()) {
            ApplicationEntity application = ApplicationEntity.builder()
                    .applicant(applicant.get())
                    .job(job.get())
                    .applicationDate(LocalDateTime.now())
                    .status(ApplicationStatus.PENDING)
                    .build();

            ApplicationEntity savedApplication = applicationRepository.save(application);

            ApplicationDto savedApplicationDto = applicationMapper.toDto(savedApplication);

            return savedApplicationDto;
        }

        return null;
    }

    @Override
    public List<ApplicationDto> getApplications(String username) {
        Optional<ApplicantEntity> applicant = applicantRepository.findByUsername(username);

        if (applicant.isEmpty()) {
            return Collections.emptyList();
        }

        List<ApplicationEntity> applicationEntities = applicationRepository
                .findByApplicant(applicant.get());

        return applicationEntities.stream()
                .map(applicationMapper::toDto)
                .toList();
    }

    @Override
    public List<ApplicationDto> getApplicationsAsEmployer(String username) {
        Optional<EmployerEntity> employer = employerRepository.findByUsername(username);

        if (employer.isEmpty()) {
            return Collections.emptyList();
        }

        List<JobEntity> jobList = jobRepository.findByEmployer(employer.get());

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
            return null;
        }

        Optional<ApplicationEntity> applicationOptional = applicationRepository.findById(id);

        if (applicationOptional.isEmpty()) {
            return null;
        }

        ApplicationEntity application = applicationOptional.get();

        if (application.getJob().getEmployer() == null ||
                !application.getJob().getEmployer().getUsername().equals(username)) {
            return null;
        }

        application.setStatus(status);

        ApplicationEntity savedApplication = applicationRepository.save(application);

        return applicationMapper.toDto(savedApplication);
    }

}
