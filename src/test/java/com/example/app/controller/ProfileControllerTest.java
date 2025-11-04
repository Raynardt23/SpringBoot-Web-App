package com.example.app.controller;

import com.example.app.DataTransferObject.ProfileUpdate;
import com.example.app.entity.Role;
import com.example.app.entity.User;
import com.example.app.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ProfileController.class)
class ProfileControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    private User makeUser() {
        return User.builder()
                .id(10L)
                .username("alice")
                .fullName("Alice Original")
                .email("alice@example.com")
                .password("encoded")
                .role(Role.ROLE_USER)
                .build();
    }

    @Test
    void getProfile_authenticated_showsProfile() throws Exception {
        User u = makeUser();
        when(userService.findByUsername("alice")).thenReturn(Optional.of(u));

        mockMvc.perform(get("/profile")
                        .with(SecurityMockMvcRequestPostProcessors.user("alice").password("pw").roles("USER"))
                )
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("user"))
                .andExpect(view().name("profile"));
    }

    @Test
    void postProfile_validData_redirectsToProfileUpdated() throws Exception {
        User u = makeUser();
        ProfileUpdate updatedReq = new ProfileUpdate("Alice New", "alice.new@example.com");

        // userService.updateProfile may be called; mock to return an updated user
        User updatedUser = makeUser();
        updatedUser.setFullName(updatedReq.fullName());
        updatedUser.setEmail(updatedReq.email());
        when(userService.updateProfile(Mockito.eq("alice"), any(ProfileUpdate.class))).thenReturn(updatedUser);

        mockMvc.perform(post("/profile")
                        .with(SecurityMockMvcRequestPostProcessors.user("alice").password("pw").roles("USER"))
                        .param("fullName", "Alice New")
                        .param("email", "alice.new@example.com")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile?updated"));
    }

    @Test
    void postProfile_invalidData_showsFormWithErrors() throws Exception {
        // missing fullName and invalid email -> validation should fail and the controller returns profile view
        mockMvc.perform(post("/profile")
                        .with(SecurityMockMvcRequestPostProcessors.user("alice").password("pw").roles("USER"))
                        .param("fullName", "") // blank -> violates @NotBlank
                        .param("email", "not-an-email")
                )
                .andExpect(status().isOk())
                .andExpect(view().name("profile"));
    }
}
