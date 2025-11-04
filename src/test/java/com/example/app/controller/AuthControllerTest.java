package com.example.app.controller;

import com.example.app.DataTransferObject.Registration;
import com.example.app.entity.Role;
import com.example.app.entity.User;
import com.example.app.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@ContextConfiguration(classes = {AuthControllerTest.TestConfig.class, AuthController.class})
class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserService userService;
    @Configuration
    static class TestConfig {
        @Bean
        UserService userService() {

            return Mockito.mock(UserService.class);
        }
    }

    @Test
    void register_happy_path_redirects_to_login() throws Exception {

        when(userService.register(any(Registration.class))).thenReturn(
                User.builder()
                        .id(1L)
                        .username("alice")
                        .email("a@example.com")
                        .fullName("Alice")
                        .password("encoded")
                        .role(Role.ROLE_USER)
                        .build()
        );

        mockMvc.perform(post("/register")
                        .param("username", "alice")
                        .param("email", "a@example.com")
                        .param("password", "password123")
                        .param("fullName", "Alice")
                        .with(csrf())) // <--- VERY IMPORTANT
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login")); // adjust to your controller's redirect
    }

    @Test
    void register_bad_input_returns_400_or_shows_form() throws Exception {
        mockMvc.perform(post("/register")
                        .param("username", "")
                        .param("email", "not-an-email")
                        .param("password", "123")
                        .param("fullName", ""))
                .andExpect(status().isOk()); // form re-rendered
    }
}
