package com.example.jobboard.controllers;

import com.example.jobboard.domain.dto.JobDto;
import com.example.jobboard.domain.dto.JobRequestDto;
import com.example.jobboard.services.JobService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

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

        return new ResponseEntity<>(Collections.emptyList(), HttpStatus.FORBIDDEN);
    }

    @PostMapping("/jobs")
    private ResponseEntity<JobDto> postJob(Authentication authentication, @Valid @RequestBody JobRequestDto job) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        for (GrantedAuthority authority : authentication.getAuthorities()) {
            String authorityString = authority.getAuthority();

            if (authorityString.equals("ROLE_EMPLOYER")) {
                JobDto savedJob = jobService.saveJob(authentication.getName(), job.getTitle());

                return new ResponseEntity<>(savedJob, HttpStatus.CREATED);
            }
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PatchMapping("/jobs/close/{id}")
    public ResponseEntity<JobDto> closeJob(@PathVariable Long id, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username = authentication.getName();

        JobDto closedJob = jobService.closeJob(id, username);

        return ResponseEntity.ok(closedJob);
    }

}
