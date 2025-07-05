package com.example.jobboard.domain.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class UserDto {

    @NotBlank(message = "Name is required and cannot be empty.")
    @Size(max = 100, message = "Name cannot exceed 100 characters.")
    private String name;

    @NotBlank(message = "Password is required and cannot be empty.")
    @Size(min = 8, message = "Password must be at least 8 characters long.")
    private String password;

    @NotBlank(message = "Username is required and cannot be empty.")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters.")
    private String username;

    @NotNull(message = "Age is required.")
    @Min(value = 0, message = "Age must be a non-negative value.")
    private Integer age;

}
