package com.example.jobboard.services;

import com.example.jobboard.domain.dto.ApplicationDto;

import java.util.List;

public interface ApplicationService {

    ApplicationDto apply(String username, Long jobId);

    List<ApplicationDto> getApplications(String username);

    List<ApplicationDto> getApplicationsAsEmployer(String name);

}
