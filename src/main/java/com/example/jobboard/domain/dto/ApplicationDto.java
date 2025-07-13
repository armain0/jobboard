package com.example.jobboard.domain.dto;

import com.example.jobboard.domain.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicationDto {

    private ApplicantDto applicant;

    private JobDto job;

    private ApplicationStatus status;

    private LocalDateTime applicationDate;

}
