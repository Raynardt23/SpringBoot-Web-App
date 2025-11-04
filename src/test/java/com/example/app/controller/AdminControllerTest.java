package com.example.app.controller;

import com.example.app.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminController.class)
class AdminControllerTest {

    @Autowired
    MockMvc mockMvc;


    @MockitoBean
    UserService userService;

    // This is the successful test ("Happy Path")
    @Test
    void adminAccess_allowedForAdmin() throws Exception {
        when(userService.listAllUsers()).thenReturn(List.of());

        mockMvc.perform(get("/admin")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk());
    }

    // This is the failed test ("Unhappy Path")
    @Test
    void adminAccess_forbiddenForUser() throws Exception {
        mockMvc.perform(get("/admin")
                        .with(user("bob").roles("USER")))
                .andExpect(status().isForbidden());
    }
}

