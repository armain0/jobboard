package com.example.jobboard.controllers;

import com.example.jobboard.domain.dto.ApplicationDto;
import com.example.jobboard.services.ApplicationService;
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
import java.util.Map;

@RestController
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping("/applications")
    public ResponseEntity<ApplicationDto> postApplication(Authentication authentication,
                                                          @RequestBody Map<String, Long> reqBody) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        for (GrantedAuthority authority : authentication.getAuthorities()) {
            String authorityString = authority.getAuthority();

            if (authorityString.equals("ROLE_APPLICANT")) {
                Long jobId = reqBody.get("id");

                ApplicationDto savedApplication = applicationService.apply(authentication.getName(), jobId);

                if (savedApplication != null) {
                    return new ResponseEntity<>(savedApplication, HttpStatus.CREATED);
                }
            }
        }

        return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);
    }

    @GetMapping("/applications")
    public ResponseEntity<List<ApplicationDto>> getApplications(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        for (GrantedAuthority authority : authentication.getAuthorities()) {
            String authorityString = authority.getAuthority();

            if (authorityString.equals("ROLE_APPLICANT")) {
                List<ApplicationDto> applications = applicationService.getApplications(authentication.getName());

                if (!applications.isEmpty()) {
                    return new ResponseEntity<>(applications, HttpStatus.OK);
                }
            } else if (authorityString.equals("ROLE_EMPLOYER")) {
                List<ApplicationDto> applications = applicationService
                        .getApplicationsAsEmployer(authentication.getName());

                if (!applications.isEmpty()) {
                    return new ResponseEntity<>(applications, HttpStatus.OK);
                }
            }
        }

        return new ResponseEntity<>(Collections.emptyList(), HttpStatus.NOT_ACCEPTABLE);
    }

}
