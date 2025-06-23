package com.example.jobboard.domain.dto;

import com.example.jobboard.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private Long id;

    private String name;

    private String password;

    private String username;

    private Integer age;

    private Role role;
}
