package com.example.app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import lombok.Builder.Default;

import java.time.Instant;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"username"}),
        @UniqueConstraint(columnNames = {"email"})
})
@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "password") // avoid printing password in logs
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Setter(AccessLevel.NONE)
    @Column(nullable = false, updatable = false)
    private String username;    // username is immutable according to instructions

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String fullName;

    @Email
    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Role role = Role.ROLE_USER;

    @Builder.Default
    private Instant createdAt = Instant.now();
}
