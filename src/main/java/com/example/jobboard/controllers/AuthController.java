package com.example.jobboard.controllers;

import com.example.jobboard.domain.dto.UserDto;
import com.example.jobboard.domain.entities.UserEntity;
import com.example.jobboard.mappers.impl.UserMapper;
import com.example.jobboard.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class AuthController {

    private final UserMapper userMapper;
    private final UserService userService;

    public AuthController(UserMapper userMapper, UserService userService) {
        this.userMapper = userMapper;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> Register(@RequestBody UserDto userDto) {
        UserEntity userEntity = userMapper.mapFrom(userDto);
        UserEntity savedUserEntity = userService.register(userEntity);

        return new ResponseEntity<>(userMapper.mapTo(savedUserEntity), HttpStatus.CREATED);
    }

}
