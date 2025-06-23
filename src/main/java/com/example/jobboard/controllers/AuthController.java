package com.example.jobboard.controllers;

import com.example.jobboard.domain.dto.UserDto;
import com.example.jobboard.domain.entities.UserEntity;
import com.example.jobboard.mappers.impl.UserMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class AuthController {

    private UserMapper userMapper;

    public AuthController(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> Register(@RequestBody UserDto userDto) {
        UserEntity userEntity = userMapper.mapFrom(userDto);


        return new ResponseEntity<>(userMapper.mapTo(userEntity), HttpStatus.CREATED);
    }

}
