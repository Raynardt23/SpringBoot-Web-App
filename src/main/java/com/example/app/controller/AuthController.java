package com.example.app.controller;

import com.example.app.DataTransferObject.Registration;
import com.example.app.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final UserService userService;
    public AuthController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegister(Model model) {
        model.addAttribute("registration", new Registration());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid Registration req, BindingResult br, Model model) {
        if (br.hasErrors()) {
            model.addAttribute("errors", br.getAllErrors());
            return "register";
        }
        try {
            userService.register(req);
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            return "register";
        }
        return "redirect:/login?registered";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
