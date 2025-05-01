package com.linkedin_clone_application.controller;

import com.linkedin_clone_application.model.User;
import com.linkedin_clone_application.repository.UserRepo;
import com.linkedin_clone_application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping("/register")
    public String registerNewUser(@ModelAttribute User user) {
       User registeredUser= userService.registerNewUser(user);
       int id=registeredUser.getId();
       return "redirect:/create_profile/"+id;
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }

    @GetMapping("/landingPage")
    public String landingPage() {
        return "landing_page";
    }

    @GetMapping("/create_profile/{Id}")
    public String createProfile(@PathVariable int Id, Model model) {
        User user = userService.findById(Id);
        model.addAttribute("user",user);
        return "complete_profile";
    }

    @PostMapping("/userDetails")
    public String userProfile(@ModelAttribute("user") User user) {
        User existingUser = userService.findById(user.getId());
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setLocation(user.getLocation());
//        User user = userService.findById(Id);
        userService.saveUser(existingUser);
        int id = user.getId();
        return "redirect:/jobDetails/" + id;
    }

    @GetMapping("/jobDetails/{Id}")
    public String jobDetails(@PathVariable int Id, Model model) {
        User user = userService.findById(Id);
        model.addAttribute("user",user);
        return "EnterJobDetails";
    }

    @PostMapping("/saveJobDetails")
    public String jobDetails(@ModelAttribute User user) {
        User existingUser = userService.findById(user.getId());
            /*existingUser.setFirstName(user.getFirstName());
            existingUser.setLastName(user.getLastName());
            existingUser.setLocation(user.getLocation());*/
        existingUser.setJobTitle(user.getJobTitle());
        existingUser.setCompanyName(user.getCompanyName());
        existingUser.setIndustry(user.getIndustry());

        userService.saveUser(user);
        int id = user.getId();
        return "redirect:/dashboard/" + id;
    }

    @GetMapping("/dashboard/{id}")
    public String dashboard(@PathVariable int id, Model model){
        User user = userService.findById(id);
        model.addAttribute("user",user);
        return "dashboard";
    }
}