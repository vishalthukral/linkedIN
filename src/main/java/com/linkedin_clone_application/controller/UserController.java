package com.linkedin_clone_application.controller;

import com.linkedin_clone_application.model.User;
import com.linkedin_clone_application.repository.UserRepo;
import com.linkedin_clone_application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model){
        User user = new User();
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping("/register")
    public String registerPost(@ModelAttribute User user){
        userService.registerNewUser(user);
        return "redirect:/login";
    }

    @GetMapping("/dashboard")
    public String dashboard(){
        return "dashboard";
    }
}
