package com.example.app.service.impl;

import com.example.app.DataTransferObject.ProfileUpdate;
import com.example.app.DataTransferObject.Registration;
import com.example.app.entity.Role;
import com.example.app.entity.User;
import com.example.app.repository.UserRepository;
import com.example.app.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User register(Registration req) {
        // Check if username already exists
        if (userRepository.existsByUsername(req.getUsername())) {
            throw new IllegalArgumentException("Username already taken");
        }

        // Check if email already exists
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("Email already used");
        }

        // Create new user
        User user = User.builder()
                .username(req.getUsername())
                .fullName(req.getFullName())
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .role(Role.ROLE_USER)
                .build();

        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User updateProfile(String username, ProfileUpdate req) {
        User u = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Use record accessors
        u.setFullName(req.fullName());
        u.setEmail(req.email());

        return userRepository.save(u);
    }

    @Override
    public List<User> listAllUsers() {
        return userRepository.findAll();
    }
}
