package com.example.app.DataTransferObject;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class Registration {

    @NotBlank
    private String username;

    @NotBlank @Email
    private String email;

    @NotBlank @Size(min = 8, max = 32)
    private String password;

    @NotBlank
    private String fullName;

    //  No-args constructor
    public Registration() {}


    public Registration(String username, String email, String password, String fullName) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
    }

    // Getters and setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
}
