package com.victorreis.msusers.unit.service;

import com.victorreis.msusers.exception.NotFoundException;
import com.victorreis.msusers.model.dto.UserRequest;
import com.victorreis.msusers.model.dto.UserResponse;
import com.victorreis.msusers.model.entity.User;
import com.victorreis.msusers.repository.UserRepository;
import com.victorreis.msusers.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.victorreis.msusers.factory.Userfactory.*;
import static org.assertj.core.api.Assertions.*;
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

        assertEquals(listResponses.size(), 2);
        verify(userRepository, times(1)).findAll();
        verifyNoMoreInteractions(userRepository);

    }

    @Test
    void shouldReturnanemptyList() {

        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        List<UserResponse> listResponses = userService.getAllUsers();

        assertEquals(listResponses.size(), 0);
        verify(userRepository, times(1)).findAll();
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("should return an user by id")
    void shouldReturnUserById() {
        User user = createUser("UserTest", 30);
        when(userRepository.findById(user.getUuid())).thenReturn(Optional.of(user));

        UserResponse victim = userService.getUserById(user.getUuid());

        assertNotNull(victim);
        assertEquals(user.getUuid(), victim.getUuid());
        assertEquals(user.getName(), victim.getName());
        assertEquals(user.getAge(), victim.getAge());

        verify(userRepository, times(1)).findById(user.getUuid());
    }


    @Test
    @DisplayName("should throw a notFoundException when userid does not exist")
    void shouldThrowAnotFoundExceptionWhenUserIdDoesNotExist() {

        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.getUserById(anyString()));
        assertTrue(exception.getMessage().contains("User not found"));
    }


    @Test
    @DisplayName("Should create a new user")
    void shouldCreateANewUser() {
        UserRequest userRequest = createUserRequest("UserTest", 25);
        User user = createUser(userRequest.getName(), userRequest.getAge());
        String uuid = UUID.randomUUID().toString();
        user.setUuid(uuid);
        when(userRepository.save(any(User.class))).thenReturn(user);

        String uuidVictim = userService.createUser(userRequest);

        assertThat(uuidVictim)
                .isNotNull()
                .isEqualTo(uuid);

        Mockito.verify(userRepository, times(1)).save(any(User.class));


    }

    @Test
    @DisplayName("Should update a user by id")
    void shouldUpdateUserById() {
        String uuid = UUID.randomUUID().toString();
        User user = createUser("User", 30);
        user.setUuid(uuid);
        UserRequest userRequest = createUserRequest("UserTest", 25);
        when(userRepository.findById(uuid)).thenReturn(Optional.of(user));

        assertThatCode(() -> userService.updateUser(uuid, userRequest))
                .doesNotThrowAnyException();

        verify(userRepository, times(1)).findById(uuid);
        verify(userRepository, times(1)).save(user);
        verifyNoMoreInteractions(userRepository);

    }

    @Test
    @DisplayName("Should throw Not Found when userId does not exist")
    void shouldThrowNotFoundExceptionWhenUserIdDoesNotExist() {
        String uuid = UUID.randomUUID().toString();
        when(userRepository.findById(uuid)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.updateUser(uuid, any(UserRequest.class)))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("User not found");

        verify(userRepository, never()).save(any(User.class));
        verify(userRepository, times(1)).findById(uuid);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("Should partial upadate an user")
    void shouldPartialUpdateAnUser() {
        String uuid = UUID.randomUUID().toString();
        UserRequest userTest = UserRequest.builder().name("Upadate User Test").build();
        User user = createUser("User", 20);

        when(userRepository.findById(uuid)).thenReturn(Optional.of(user));

        assertThatCode(()-> userService.partialUpdateUser(uuid,userTest)).doesNotThrowAnyException();

        verify(userRepository,times(1)).findById(uuid);
        verify(userRepository,times(1)).save(any(User.class));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("Should not partial update an user due to a Not Found Exception")
    void shouldNotPartialUpdateAnUserDueToANotFoundException(){
        String uuid = UUID.randomUUID().toString();
        UserRequest userTest = UserRequest.builder().name("Upadate User Test").build();

        when(userRepository.findById(uuid)).thenReturn(Optional.empty());

        assertThatThrownBy(()->userService.partialUpdateUser(uuid,userTest))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("User not found");

        verify(userRepository, times(1)).findById(uuid);
        verify(userRepository, never()).save(any(User.class));
        verifyNoMoreInteractions(userRepository);

    }

    @Test
    @DisplayName("Should delete an user by id")
    void shouldDeleteAnUserById(){
        String uuid = UUID.randomUUID().toString();
        User user = createUser("User Test", 25);

        when(userRepository.findById(uuid)).thenReturn(Optional.of(user));

        assertThatCode(()-> userService.deleteUserById(uuid)).doesNotThrowAnyException();

        verify(userRepository, times(1)).findById(uuid);
        verify(userRepository, times(1)).delete(any(User.class));
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("Should not delete an user by id because it does not exist")
    void shouldNotDeleteAnUserByIdBecauseItDoesNotExist(){
        String uuid = UUID.randomUUID().toString();

        when(userRepository.findById(uuid)).thenReturn(Optional.empty());

        assertThatCode(()-> userService.deleteUserById(uuid)).doesNotThrowAnyException();

        verify(userRepository, times(1)).findById(uuid);
        verify(userRepository, never()).delete(any(User.class));
        verifyNoMoreInteractions(userRepository);
    }
}
