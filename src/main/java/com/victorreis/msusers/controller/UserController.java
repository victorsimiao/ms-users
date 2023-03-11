package com.victorreis.msusers.controller;

import com.victorreis.msusers.model.dto.UserResponse;
import com.victorreis.msusers.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
