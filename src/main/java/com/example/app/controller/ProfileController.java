package com.example.app.controller;

import com.example.app.DataTransferObject.ProfileUpdate;
import com.example.app.entity.User;
import com.example.app.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class ProfileController {

    private final UserService userService;
    public ProfileController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String profile(@AuthenticationPrincipal UserDetails principal, Model model) {
        if (principal == null) return "redirect:/login";
        User u = userService.findByUsername(principal.getUsername()).orElseThrow();
        model.addAttribute("user", u);
        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@AuthenticationPrincipal UserDetails principal,
                                @Valid ProfileUpdate req,
                                BindingResult br,
                                Model model) {
        if (br.hasErrors()) {
            model.addAttribute("errors", br.getAllErrors());
            return "profile";
        }
        try {
            userService.updateProfile(principal.getUsername(), req);
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            return "profile";
        }
        return "redirect:/profile?updated";
    }
}
