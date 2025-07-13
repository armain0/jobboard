package com.example.jobboard.domain.dto;

import com.example.jobboard.domain.JobStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobDto {

    private Long id;

    private String title;

    private JobStatus status;

    private EmployerDto employer;

}
