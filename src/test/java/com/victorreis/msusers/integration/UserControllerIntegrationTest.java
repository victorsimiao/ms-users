package com.victorreis.msusers.integration;

import com.victorreis.msusers.model.entity.User;
import com.victorreis.msusers.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@IntegrationTest
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

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
                .andExpect(jsonPath("$[*].name",containsInAnyOrder("Marcos","Luis")))
                .andExpect(jsonPath("$[*].age",containsInAnyOrder(30,40)));

    }

}
