package com.example.jobboard.services.impl;

import com.example.jobboard.domain.JobStatus;
import com.example.jobboard.domain.dto.JobDto;
import com.example.jobboard.domain.entities.EmployerEntity;
import com.example.jobboard.domain.entities.JobEntity;
import com.example.jobboard.exceptions.ResourceNotFoundException;
import com.example.jobboard.mappers.JobMapper;
import com.example.jobboard.repositories.EmployerRepository;
import com.example.jobboard.repositories.JobRepository;
import com.example.jobboard.services.JobService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final JobMapper jobMapper;

    private final EmployerRepository employerRepository;

    public JobServiceImpl(JobRepository jobRepository, JobMapper jobMapper, EmployerRepository employerRepository) {
        this.jobRepository = jobRepository;
        this.jobMapper = jobMapper;
        this.employerRepository = employerRepository;
    }

    @Override
    public List<JobDto> getAllJobs() {
        List<JobEntity> jobEntities = StreamSupport.stream(
                jobRepository
                        .findAll()
                        .spliterator(), false).toList();

        return jobEntities.stream()
                .map(jobMapper::toDto)
                .toList();
    }

    @Override
    public List<JobDto> getAllJobsAsEmployer(String username) {
        EmployerEntity employer = employerRepository.findByUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employer with username '" + username + "' not found."));

        List<JobEntity> jobEntities = jobRepository
                .findByEmployer(employer);

        return jobEntities.stream()
                .map(jobMapper::toDto)
                .toList();
    }

    @Override
    public JobDto saveJob(String employerUsername, String title) {
        EmployerEntity employer = employerRepository.findByUsername(employerUsername)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employer with username '" + employerUsername + "' not found."));

        JobEntity job = JobEntity.builder()
                .employer(employer)
                .status(JobStatus.OPEN)
                .title(title).build();

        JobEntity savedJob = jobRepository.save(job);

        return jobMapper.toDto(savedJob);
    }

    @Override
    public JobDto closeJob(Long jobId, String username) {
        JobEntity jobEntity = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Id " + jobId + " not found."));

        if (jobEntity.getEmployer() == null || !jobEntity.getEmployer().getUsername().equals(username)) {
            throw new AccessDeniedException("User '" + username + "' is not authorized to close job with ID " + jobId + ".");
        }

        jobEntity.setStatus(JobStatus.CLOSED);
        JobEntity updatedJobEntity = jobRepository.save(jobEntity);

        return jobMapper.toDto(updatedJobEntity);
    }
}
