package com.example.jobboard.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobRequestDto {

    @NotBlank(message = "Title is required and cannot be empty.")
    @Size(max = 50, message = "Title cannot exceed 50 characters.")
    private String title;

}
