package com.example.jobboard.services.impl;

import com.example.jobboard.domain.dto.JobDto;
import com.example.jobboard.domain.entities.EmployerEntity;
import com.example.jobboard.domain.entities.JobEntity;
import com.example.jobboard.mappers.JobMapper;
import com.example.jobboard.repositories.EmployerRepository;
import com.example.jobboard.repositories.JobRepository;
import com.example.jobboard.services.JobService;
import org.springframework.stereotype.Service;

import java.util.Collections;
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
                .map(jobMapper::jobToJobDto)
                .toList();
    }

    @Override
    public List<JobDto> getAllJobsWithEmployer(String username) {
        EmployerEntity employer = employerRepository.findByUsername(username);

        if (employer == null) {
            return Collections.emptyList();
        }

        List<JobEntity> jobEntities = jobRepository
                .findByEmployer(employer);

        return jobEntities.stream()
                .map(jobMapper::jobToJobDto)
                .toList();
    }

    @Override
    public JobDto saveJob(String employerUsername) {
        EmployerEntity employer = employerRepository.findByUsername(employerUsername);

        JobEntity job = new JobEntity();
        job.setEmployer(employer);

        JobEntity savedJob = jobRepository.save(job);

        return jobMapper.jobToJobDto(savedJob);
    }
}
