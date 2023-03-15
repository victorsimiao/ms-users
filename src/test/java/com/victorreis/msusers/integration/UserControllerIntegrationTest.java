package com.victorreis.msusers.integration;

import com.victorreis.msusers.integration.core.IntegrationTest;
import com.victorreis.msusers.integration.factory.Userfactory;
import com.victorreis.msusers.model.entity.User;
import com.victorreis.msusers.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.jdbc.JdbcTestUtils.deleteFromTables;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String USERS_ENDPOINT = "/users";
    private static final String USERS_BY_ID_ENDPOINT = "/users/{userId}";

    @BeforeEach
    void setup() {
        deleteFromTables(jdbcTemplate, "users");
    }

    @Test
    @DisplayName("Should return all users")
    void shoulRetunAllUsers() throws Exception {

        User userOne = User.builder().name("Marcos").age(30).build();
        User userTwo = User.builder().name("Luis").age(40).build();
        userRepository.save(userOne);
        userRepository.save(userTwo);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder("Marcos", "Luis")))
                .andExpect(jsonPath("$[*].age", containsInAnyOrder(30, 40)));

    }

    @Test
    @DisplayName("Should return an user by id")
    void shouldRetunrAnUserById() throws Exception {
        User userTest = Userfactory.createUser("userTest", 30);
        userRepository.save(userTest);
        String uuid = userTest.getUuid();


        mockMvc.perform(get(USERS_BY_ID_ENDPOINT,uuid))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name",is(userTest.getName())))
                .andExpect(jsonPath("$.age",is(userTest.getAge())))
                .andExpect(jsonPath("$.uuid",hasLength(uuid.length())));
    }


    @Test
    @DisplayName("Should return 404 NotFound when the user does not exist")
    void shouldReturn404NotFoundWhenTheUserDoesNotExist() throws Exception {
        String invalidUuid = UUID.randomUUID().toString();


        mockMvc.perform(get(USERS_BY_ID_ENDPOINT,invalidUuid))
                .andExpect(status().isNotFound());
    }

}
