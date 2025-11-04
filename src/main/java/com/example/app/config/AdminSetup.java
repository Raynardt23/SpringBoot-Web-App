package com.example.app.config;

import com.example.app.entity.User;
import com.example.app.entity.Role;
import com.example.app.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class AdminSetup {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminSetup(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = User.builder()
                    .username("admin")
                    .email("admin@example.com")
                    .fullName("Administrator")
                    .password(passwordEncoder.encode("admin123"))
                    .role(Role.ROLE_ADMIN)
                    .build();

            userRepository.save(admin);
            System.out.println(" Default admin user created: admin / admin123");

        } else {
            System.out.println("Admin user already exists");
        }
    }
}
