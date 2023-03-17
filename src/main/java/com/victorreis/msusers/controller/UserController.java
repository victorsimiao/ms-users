package com.victorreis.msusers.controller;

import com.victorreis.msusers.model.dto.UserRequest;
import com.victorreis.msusers.model.dto.UserResponse;
import com.victorreis.msusers.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static java.lang.String.format;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{userid}")
    public ResponseEntity<UserResponse> getUserByid(@PathVariable("userid") String userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody @Valid UserRequest userRequest) {
        String createdUserId = userService.createUser(userRequest);
        URI location = URI.create(format("/users/%s", createdUserId));
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Void> updateUser(@PathVariable(name = "userId") String userId, @RequestBody @Valid UserRequest userRequest) {
        userService.updateUser(userId, userRequest);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Void> partialUpdateUser(@PathVariable(name = "userId") String userId, @RequestBody UserRequest userRequest) {
        userService.partialUpdateUser(userId, userRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteById(@PathVariable(name = "userId") String userId) {
        userService.deleteUserById(userId);
        return ResponseEntity.noContent().build();
    }
}
