package com.example.jobboard.services.impl;

import com.example.jobboard.domain.entities.UserEntity;
import com.example.jobboard.repositories.UserRepository;
import com.example.jobboard.services.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserEntity register(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }

}
