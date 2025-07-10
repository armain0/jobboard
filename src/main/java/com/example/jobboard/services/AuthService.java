package com.example.jobboard.services;

import com.example.jobboard.domain.dto.LoginRequestDto;
import com.example.jobboard.domain.dto.LoginResponseDto;
import jakarta.validation.Valid;

public interface AuthService {
    LoginResponseDto authenticateUser(@Valid LoginRequestDto loginRequestDto);
}
