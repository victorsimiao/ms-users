package com.victorreis.msusers.factory;

import com.victorreis.msusers.model.dto.UserRequest;
import com.victorreis.msusers.model.entity.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public final class Userfactory {
    private Userfactory() {
    }

    public static User createUser(String name, int age) {
        return User.builder()
                .name(name)
                .age(age)
                .build();
    }

    public static UserRequest createUserRequest(String name, int age) {
        return UserRequest.builder()
                .name(name)
                .age(age)
                .build();
    }

    public static List<User> newUserList() {
        return List.of(
                User.builder().uuid(UUID.randomUUID().toString()).name("Luis").age(28).createdAt(LocalDateTime.now()).updatedAT(LocalDateTime.now()).build(),
                User.builder().uuid(UUID.randomUUID().toString()).name("Maria").age(20).createdAt(LocalDateTime.now()).updatedAT(LocalDateTime.now()).build()
        );
    }
}
