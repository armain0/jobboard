package com.example.jobboard.services;

import com.example.jobboard.domain.dto.JobDto;

import java.util.List;

public interface JobService {
    List<JobDto> getAllJobs();

    List<JobDto> getAllJobsWithEmployer(String name);

    JobDto saveJob(String employerUsername);
}
