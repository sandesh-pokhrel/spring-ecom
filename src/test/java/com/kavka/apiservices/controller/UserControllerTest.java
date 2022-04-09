package com.kavka.apiservices.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kavka.apiservices.config.TestSecurityConfig;
import com.kavka.apiservices.model.User;
import com.kavka.apiservices.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {TestSecurityConfig.class}) // skipping security for testRestTemplate
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc(addFilters = false) // skipping security for mockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private UserService userService;

    private User user;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        user = User.builder()
                .email("hello@email.com")
                .firstName("first name")
                .lastName("last name")
                .build();
    }

    @Test
    void saveUser_bindFailed() throws Exception {
        // setting email to null
        user.setEmail(null);

        doReturn(user).when(userService).saveUser(user);
        mockMvc
                .perform(
                        post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(user)))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    void saveUser() throws Exception {
        doReturn(user).when(userService).saveUser(user);
        mockMvc
                .perform(
                        post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(result -> verify(userService, times(1)).saveUser(any()))
                .andExpect(jsonPath("firstName").exists());
    }

    @Test
    void verifyUser() throws Exception {
        // given
        user.setId(1001);
        user.setIsVerified(true);

        when(userService.getById(anyInt())).thenReturn(user);
        doReturn(user).when(userService).verifyUser(any());

        ResponseEntity<User> response = restTemplate.exchange("/users/verify/" + user.getId(), HttpMethod.PUT, null, User.class);
        assertThat(Objects.requireNonNull(response.getBody()).getIsVerified()).isTrue();

//        mockMvc
//                .perform(put("/users/verify/1001"))
//                .andExpect(jsonPath("id").exists());
    }
}
