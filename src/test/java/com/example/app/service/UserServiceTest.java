package com.example.app.service;

import com.example.app.DataTransferObject.ProfileUpdate;
import com.example.app.DataTransferObject.Registration;
import com.example.app.entity.Role;
import com.example.app.entity.User;
import com.example.app.repository.UserRepository;
import com.example.app.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceTest {

    @Test
    void register_sets_password_and_saves() {
        UserRepository repo = Mockito.mock(UserRepository.class);
        when(repo.existsByUsername("bob")).thenReturn(false);
        when(repo.existsByEmail("b@example.com")).thenReturn(false);
        when(repo.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var encoder = new BCryptPasswordEncoder();
        var svc = new UserServiceImpl(repo, encoder);

        var req = new Registration("bob", "b@example.com", "password123", "Bob");
        User saved = svc.register(req);

        assertNotNull(saved);
        assertEquals("bob", saved.getUsername());
        assertEquals("Bob", saved.getFullName());
        assertNotEquals("password123", saved.getPassword());
        assertTrue(encoder.matches("password123", saved.getPassword()));
    }

    @Test
    void register_throws_on_duplicate_username() {
        UserRepository repo = Mockito.mock(UserRepository.class);
        when(repo.existsByUsername("bob")).thenReturn(true);
        var svc = new UserServiceImpl(repo, new BCryptPasswordEncoder());
        var req = new Registration("bob","b@example.com","password","Bob");
        assertThrows(IllegalArgumentException.class, () -> svc.register(req));
    }


    @Test
    void findByUsername_returns_user_when_present() {
        UserRepository repo = Mockito.mock(UserRepository.class);

        User u = User.builder()
                .id(1L)
                .username("alice")
                .fullName("Alice")
                .email("a@example.com")
                .password("encoded")
                .role(Role.ROLE_USER)
                .build();

        when(repo.findByUsername("alice")).thenReturn(Optional.of(u));

        var svc = new UserServiceImpl(repo, new BCryptPasswordEncoder());
        Optional<User> got = svc.findByUsername("alice");

        assertTrue(got.isPresent());
        assertEquals("alice", got.get().getUsername());
    }

    @Test
    void updateProfile_changes_fullname_and_email_and_saves() {
        UserRepository repo = Mockito.mock(UserRepository.class);

        User u = User.builder()
                .id(2L)
                .username("charlie")
                .fullName("Charlie Old")
                .email("old@example.com")
                .password("encoded")
                .role(Role.ROLE_USER)
                .build();

        when(repo.findByUsername("charlie")).thenReturn(Optional.of(u));
        when(repo.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var svc = new UserServiceImpl(repo, new BCryptPasswordEncoder());
        var req = new ProfileUpdate("Charlie New", "new@example.com");
        User updated = svc.updateProfile("charlie", req);

        assertEquals("Charlie New", updated.getFullName());
        assertEquals("new@example.com", updated.getEmail());
        verify(repo).save(updated);
    }

    @Test
    void listAllUsers_returns_all() {
        UserRepository repo = Mockito.mock(UserRepository.class);

        User u1 = User.builder()
                .id(1L)
                .username("u1")
                .fullName("User One")
                .email("u1@example.com")
                .password("encoded1")
                .role(Role.ROLE_USER)
                .build();

        User u2 = User.builder()
                .id(2L)
                .username("u2")
                .fullName("User Two")
                .email("u2@example.com")
                .password("encoded2")
                .role(Role.ROLE_USER)
                .build();

        when(repo.findAll()).thenReturn(List.of(u1, u2));

        var svc = new UserServiceImpl(repo, new BCryptPasswordEncoder());
        var list = svc.listAllUsers();

        assertEquals(2, list.size());
        assertTrue(list.stream().anyMatch(u -> "u1".equals(u.getUsername())));
    }

}
