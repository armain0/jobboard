package com.example.jobboard.controllers;

import com.example.jobboard.domain.dto.JobDto;
import com.example.jobboard.services.JobService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping("/jobs")
    private ResponseEntity<List<JobDto>> getJobs(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        for (GrantedAuthority authority : authentication.getAuthorities()) {
            String authorityString = authority.getAuthority();

            if ("ROLE_EMPLOYER".equals(authorityString)) {
                List<JobDto> jobs = jobService.getAllJobsAsEmployer(authentication.getName());

                return new ResponseEntity<>(jobs, HttpStatus.OK);
            } else if ("ROLE_APPLICANT".equals(authorityString)) {
                List<JobDto> jobs = jobService.getAllJobs();

                return new ResponseEntity<>(jobs, HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(Collections.emptyList(), HttpStatus.NOT_ACCEPTABLE);
    }

    @PostMapping("/jobs")
    private ResponseEntity<JobDto> postJob(Authentication authentication, @RequestBody JobDto job) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        for (GrantedAuthority authority : authentication.getAuthorities()) {
            String authorityString = authority.getAuthority();

            if (authorityString.equals("ROLE_EMPLOYER")) {
                JobDto savedJob = jobService.saveJob(authentication.getName(), job);

                if (savedJob != null) {
                    return new ResponseEntity<>(savedJob, HttpStatus.CREATED);
                }
            }
        }

        return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
    }

}
