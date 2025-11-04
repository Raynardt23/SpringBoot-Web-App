package com.example.app.service;

import com.example.app.DataTransferObject.ProfileUpdate;
import com.example.app.DataTransferObject.Registration;
import com.example.app.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User register(Registration req);
    Optional<User> findByUsername(String username);
    User updateProfile(String username, ProfileUpdate req);
    List<User> listAllUsers();
}
