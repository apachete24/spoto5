package com.grupor.spoto5.controller;

import com.grupor.spoto5.model.User;
import com.grupor.spoto5.repository.UserRepository;
import com.grupor.spoto5.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.Optional;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @ModelAttribute
    public void addAttributes(Model model, HttpServletRequest request) {

        Principal principal = request.getUserPrincipal();

        if (principal != null) {

            model.addAttribute("logged", true);
            model.addAttribute("currentUser", principal.getName());
            model.addAttribute("admin", request.isUserInRole("ADMIN"));

        } else {
            model.addAttribute("logged", false);
        }
    }

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
        try {
            userService.saveUser(username, password);
            return "redirect:/index";
        } catch (DuplicateKeyException ex) {
            String errorMessage = ex.getMessage();
            model.addAttribute("errorMessage", errorMessage);
            return "register";
        }
    }

    @GetMapping("/user")
    public String currentUser (Model model) {
        boolean logged = (boolean) model.getAttribute("logged");
        Optional<User> user = userService.findByName((String) model.getAttribute("currentUser"));
        if (user.isPresent()) {
            User userAux = user.get();
            model.addAttribute("userID", userAux.getId());
            if (logged) {
                return "user";
            } else {
                return "denied";
            }
        } else {
            return "error";
        }
    }

    @GetMapping("/adminpage")
    public String adminPage (Model model) {
        boolean isAdmin = (boolean) model.getAttribute("admin");
        if (isAdmin) {
            model.addAttribute("users", userService.findAll());
            return "users_admin";
        } else {
            return "denied";
        }
    }

    @GetMapping("/deleteuser/{id}")
    public String deleteUser(Model model, @PathVariable long id) {

        boolean isAdmin = (boolean) model.getAttribute("admin");

        userService.deleteUser(id, isAdmin);
        return"redirect:/adminpage";

    }

    @GetMapping("/edituser/{id}")
    public String editUser (Model model, @PathVariable Long id) {

        Optional<User> userAux = userService.findById(id);

        if (userAux.isPresent()) {
            User user = userAux.get();
            if (user.getName().equals(model.getAttribute("currentUser"))) {
                model.addAttribute("userID", user.getId());
                return "edit_user";
            } else {
                return "denied";
            }
        } else {
            return "error";
        }

    }

    @PostMapping("/edituser/{id}")
    public String editUser (Model model, @PathVariable long id, String username, String password) {
        String currentUser = (String) model.getAttribute("currentUser");
        userService.updateUser(id, username, password, currentUser);
        return "/user";
    }
}


