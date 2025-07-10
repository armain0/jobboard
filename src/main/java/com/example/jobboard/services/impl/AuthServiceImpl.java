package com.example.jobboard.services.impl;

import com.example.jobboard.domain.dto.LoginRequestDto;
import com.example.jobboard.domain.dto.LoginResponseDto;
import com.example.jobboard.services.AuthService;
import com.example.jobboard.services.JwtTokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;

    public AuthServiceImpl(AuthenticationManager authenticationManager, JwtTokenService jwtTokenService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    public LoginResponseDto authenticateUser(LoginRequestDto loginRequestDto) {
        var token = new UsernamePasswordAuthenticationToken(
                loginRequestDto.getUsername(), loginRequestDto.getPassword()
        );

        Authentication authentication = authenticationManager.authenticate(token);

        String jwtToken = jwtTokenService.generateToken(authentication);
        Long expiration = jwtTokenService.expiresAt(jwtToken);

        return LoginResponseDto.builder()
                .message("Login successful.")
                .jwtToken(jwtToken)
                .name(authentication.getName())
                .authorities(authentication.getAuthorities())
                .expiration(expiration)
                .build();
    }

}
