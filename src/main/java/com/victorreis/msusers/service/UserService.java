package com.victorreis.msusers.service;

import com.victorreis.msusers.model.dto.UserResponse;
import com.victorreis.msusers.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserResponse> getAllUsers() {
        log.info("Getting all users...");
        return userRepository.findAll().stream().map(UserResponse::toResponse).toList();
    }
}
