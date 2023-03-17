package com.victorreis.msusers.service;

import com.victorreis.msusers.exception.NotFoundException;
import com.victorreis.msusers.model.dto.UserRequest;
import com.victorreis.msusers.model.dto.UserResponse;
import com.victorreis.msusers.model.entity.User;
import com.victorreis.msusers.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


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

    public UserResponse getUserById(String userId) {
        return userRepository.findById(userId)
                .map(UserResponse::toResponse)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    public String createUser(UserRequest userRequest) {
        log.info("Creating a new user with: {}", userRequest);
        User user = User.valueOf(userRequest);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAT(LocalDateTime.now());

        User saveEntity = userRepository.save(user);

        return saveEntity.getUuid();
    }

    public void updateUser(String userId, UserRequest userRequest) {
        log.info("Updating userId: {} with {}", userId, userRequest);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        user.updateValues(userRequest);

        userRepository.save(user);
    }

    public void partialUpdateUser(String userId, UserRequest userRequest) {
        log.info("Partial Updating userId: {} with {}", userId, userRequest);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Optional.ofNullable(userRequest.getName()).ifPresent(user::setName);
        Optional.ofNullable(userRequest.getAge()).ifPresent(user::setAge);

        user.setUpdatedAT(LocalDateTime.now());

        userRepository.save(user);
    }

}
