package com.victorreis.msusers.integration;

import com.victorreis.msusers.integration.core.IntegrationTest;
import com.victorreis.msusers.model.entity.User;
import com.victorreis.msusers.repository.UserRepository;
import com.victorreis.msusers.utils.FilesUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static com.victorreis.msusers.factory.Userfactory.createUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.jdbc.JdbcTestUtils.deleteFromTables;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
        User userTest = createUser("userTest", 30);
        userRepository.save(userTest);
        String uuid = userTest.getUuid();


        mockMvc.perform(get(USERS_BY_ID_ENDPOINT, uuid))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(userTest.getName())))
                .andExpect(jsonPath("$.age", is(userTest.getAge())))
                .andExpect(jsonPath("$.uuid", hasLength(uuid.length())));
    }


    @Test
    @DisplayName("Should return 404 NotFound when the user does not exist")
    void shouldReturn404NotFoundWhenTheUserDoesNotExist() throws Exception {
        String invalidUuid = UUID.randomUUID().toString();


        mockMvc.perform(get(USERS_BY_ID_ENDPOINT, invalidUuid))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should create a new user")
    void shouldCreateANewUser() throws Exception {
        String requestBody = FilesUtils.getJsonFromFile("createUser.json");


        mockMvc.perform(post(USERS_ENDPOINT)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

    }

    @Test
    @DisplayName("Should update an existing user and return no content")
    void shouldUpdateAnExistingUserAndReturnNoContent() throws Exception {
        User userPutTest = createUser("UserPutTest", 28);
        userRepository.save(userPutTest);
        String uuid = userPutTest.getUuid();

        String requestBody = FilesUtils.getJsonFromFile("updateUser.json");

        mockMvc.perform(put(USERS_BY_ID_ENDPOINT, uuid)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isNoContent());

        User updateUser = userRepository.findById(uuid).get();
        assertThat(updateUser.getUuid()).isNotNull();
        assertThat(updateUser.getName()).isEqualTo("Update User Integration Test");
        assertThat(updateUser.getAge()).isEqualTo(31);
    }

    @Test
    @DisplayName("Should return not found when the userId is invalid")
    void shouldReturnNotFoundWhenTheUserIdIsInvalid() throws Exception {
        String uuid = UUID.randomUUID().toString();

        String resquestBody = FilesUtils.getJsonFromFile("updateUser.json");

        mockMvc.perform(put(USERS_BY_ID_ENDPOINT, uuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(resquestBody))
                
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return bad request when name key is not present")
    void shouldReturnBadRequestWhenNameKeyIsNotPresent() throws Exception {
        User userPutTest = createUser("UserPutTest", 28);
        userRepository.save(userPutTest);
        String uuid = userPutTest.getUuid();

        String resquestBody = FilesUtils.getJsonFromFile("updateUserWithOutName.json");

        mockMvc.perform(put(USERS_BY_ID_ENDPOINT, uuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(resquestBody))

                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return bad request when age key is not present")
    void shouldReturnBadRequestWhenAgeKeyIsNotPresent() throws Exception {
        User userPutTest = createUser("UserPutTest", 28);
        userRepository.save(userPutTest);
        String uuid = userPutTest.getUuid();

        String resquestBody = FilesUtils.getJsonFromFile("updateUserWithOutAge.json");

        mockMvc.perform(put(USERS_BY_ID_ENDPOINT, uuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(resquestBody))

                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should partially update an existing user name")
    void shouldPartiallyUpadateAnExistingUserName() throws Exception {
        User userPutTest = createUser("UserPatchNameTest", 33);
        userRepository.save(userPutTest);
        String uuid = userPutTest.getUuid();

        String requestBody = FilesUtils.getJsonFromFile("partiallyUpdateUserName.json");

        mockMvc.perform(patch(USERS_BY_ID_ENDPOINT, uuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNoContent());

        User updateUser = userRepository.findById(uuid).get();
        assertThat(updateUser.getUuid()).isNotNull();
        assertThat(updateUser.getName()).isEqualTo("Partially Update User Integration Test");
        assertThat(updateUser.getAge()).isEqualTo(33);
    }

    @Test
    @DisplayName("Should partially update an existing user age")
    void shouldPartiallyUpadateAnExistingUserAge() throws Exception {
        User userPutTest = createUser("UserPatchAgeTest", 33);
        userRepository.save(userPutTest);
        String uuid = userPutTest.getUuid();

        String requestBody = FilesUtils.getJsonFromFile("partiallyUpdateUserAge.json");

        mockMvc.perform(patch(USERS_BY_ID_ENDPOINT, uuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNoContent());

        User updateUser = userRepository.findById(uuid).get();
        assertThat(updateUser.getUuid()).isNotNull();
        assertThat(updateUser.getName()).isEqualTo("UserPatchAgeTest");
        assertThat(updateUser.getAge()).isEqualTo(30);
    }

}
