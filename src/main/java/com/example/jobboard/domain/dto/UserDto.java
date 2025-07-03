package com.example.jobboard.domain.dto;

import com.example.jobboard.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class UserDto {

    private Long id;

    private String name;

    private String password;

    private String username;

    private Integer age;

    private Role role;
}
