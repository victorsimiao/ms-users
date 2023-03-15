package com.victorreis.msusers.unit.service;

import com.victorreis.msusers.exception.NotFoundException;
import com.victorreis.msusers.model.dto.UserResponse;
import com.victorreis.msusers.model.entity.User;
import com.victorreis.msusers.repository.UserRepository;
import com.victorreis.msusers.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("User Service Test")
public class UserServiceUnitTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Should return a list with all users")
    void shouldRetunrAlistWithAllUsers() {

        when(userRepository.findAll()).thenReturn(newUserList());

        List<UserResponse> listResponses = userService.getAllUsers();

        assertEquals(listResponses.size(),2);
        verify(userRepository,times(1)).findAll();
        verifyNoMoreInteractions(userRepository);

    }

    @Test
    void shouldReturnanemptyList(){

        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        List<UserResponse> listResponses = userService.getAllUsers();

        assertEquals(listResponses.size(),0);
        verify(userRepository,times(1)).findAll();
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("should return an user by id")
    void shouldReturnUserById(){
        User user = newUser();
        when(userRepository.findById(user.getUuid())).thenReturn(Optional.of(user));

        UserResponse victim = userService.getUserById(user.getUuid());

        assertNotNull(victim);
        assertEquals(user.getUuid(),victim.getUuid());
        assertEquals(user.getName(),victim.getName());
        assertEquals(user.getAge(),victim.getAge());

        verify(userRepository,times(1)).findById(user.getUuid());
    }


    @Test
    @DisplayName("should throw a notFoundException when userid does not exist")
    void shouldThrowAnotFoundExceptionWhenUserIdDoesNotExist(){

        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.getUserById(anyString()));
        assertTrue(exception.getMessage().contains("User not found"));
    }



    private List<User> newUserList() {
        return List.of(
                User.builder().uuid(UUID.randomUUID().toString()).name("Luis").age(28).createdAt(LocalDateTime.now()).updatedAT(LocalDateTime.now()).build(),
                User.builder().uuid(UUID.randomUUID().toString()).name("Maria").age(20).createdAt(LocalDateTime.now()).updatedAT(LocalDateTime.now()).build()
        );
    }

    private User newUser(){
        return User.builder()
                .uuid(UUID.randomUUID().toString())
                .name("Victor")
                .age(30)
                .build();
    }
}
