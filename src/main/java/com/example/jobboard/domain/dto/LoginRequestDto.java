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
public class LoginRequestDto {

    @NotBlank(message = "Name is required and cannot be empty.")
    @Size(max = 100, message = "Name cannot exceed 100 characters.")
    private String username;

    @NotBlank(message = "Password is required and cannot be empty.")
    @Size(min = 8, message = "Password must be at least 8 characters long.")
    private String password;

}
