package com.example.jobboard.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyDto {

    @NotBlank(message = "Company name is required.")
    @Size(max = 255, message = "Company name cannot exceed 255 characters.")
    private String name;

    @NotBlank(message = "Website is required.")
    @Pattern(regexp = "^[a-zA-Z0-9-]{1,25}\\.[a-zA-Z]{2,3}$", message = "Invalid website format. Must be like 'example.com' (max 25 chars before dot, 2-3 letters after).")
    private String website;

    @NotBlank(message = "Industry is required.")
    @Size(max = 100, message = "Industry cannot exceed 100 characters.")
    private String industry;

}
