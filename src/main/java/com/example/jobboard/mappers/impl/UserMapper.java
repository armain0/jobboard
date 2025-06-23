package com.example.jobboard.mappers.impl;

import com.example.jobboard.domain.dto.UserDto;
import com.example.jobboard.domain.entities.UserEntity;
import com.example.jobboard.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapper implements Mapper<UserEntity, UserDto> {

    private ModelMapper modelMapper;

    public UserMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public UserEntity mapFrom(UserDto userDto) {
        return modelMapper.map(userDto, UserEntity.class);
    }

    @Override
    public UserDto mapTo(UserEntity userEntity) {
        return modelMapper.map(userEntity, UserDto.class);
    }

}
