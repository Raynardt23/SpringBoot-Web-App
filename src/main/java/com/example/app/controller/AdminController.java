package com.example.app.controller;

import com.example.app.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {
    private final UserService userService;
    public AdminController(UserService userService){ this.userService = userService; }

    @GetMapping("/admin")
    public String adminDashboard(Model model){
        model.addAttribute("users", userService.listAllUsers());
        return "admin";
    }
}
