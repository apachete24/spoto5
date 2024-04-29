package com.grupor.spoto5.controller;

import com.grupor.spoto5.model.User;
import com.grupor.spoto5.repository.UserRepository;
import com.grupor.spoto5.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/loginerror")
    public String loginerror() {
        return "loginerror";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/user/{id}")
    public String user () {
        return "user";
    }

    @PostMapping("/register")
    public String newUser(String username, String password, Model model) {
        userService.saveUser(username, password);
        return "redirect:/index";
    }

}


