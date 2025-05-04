package com.linkedin_clone_application.controller;

import com.linkedin_clone_application.Util.TimeAgoUtil;
import com.linkedin_clone_application.model.Post;
import com.linkedin_clone_application.model.User;
import com.linkedin_clone_application.repository.PostRepo;
import com.linkedin_clone_application.service.CloudinaryService;
import com.linkedin_clone_application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/")
public class UserController {

    private final UserService userService;
    private final CloudinaryService cloudinaryService;
    private final PostRepo postRepo;

    @Autowired
    public UserController(UserService userService, CloudinaryService cloudinaryService, PostRepo postRepo) {
        this.userService = userService;
        this.cloudinaryService = cloudinaryService;
        this.postRepo = postRepo;
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
        User registeredUser = userService.registerNewUser(user);
        int id = registeredUser.getId();
        return "redirect:/create_profile/" + id;
    }

    @GetMapping("/landingPage")
    public String landingPage() {
        return "landing_page";
    }

    @GetMapping("/create_profile/{Id}")
    public String createProfile(@PathVariable int Id, Model model) {
        User user = userService.findById(Id);
        model.addAttribute("user", user);
        return "complete_profile";
    }

    @PostMapping("/userDetails")
    public String userProfile(@ModelAttribute("user") User user, @RequestParam("image") MultipartFile image) throws IOException {
        if (image != null && !image.isEmpty()) {
            String imageUrl = cloudinaryService.uploadImage(image);
            user.setProfilePictureUrl(imageUrl);
        }
        userService.saveUser(user);
        int id = user.getId();
        return "redirect:/jobDetails/" + id;
    }

    @GetMapping("/jobDetails/{Id}")
    public String jobDetails(@PathVariable int Id, Model model) {
        User user = userService.findById(Id);
        model.addAttribute("user", user);
        return "EnterJobDetails";
    }

    @PostMapping("/saveJobDetails")
    public String jobDetails(@ModelAttribute User user) {
        User existingUser = userService.findById(user.getId());
        userService.saveUser(user);
        int id = user.getId();
        return "redirect:/login";
    }

    @GetMapping("/dashboard/{id}")
    public String userDashboard(@PathVariable int id, Model model) {
        User user = userService.findById(id); // Get the user by ID
        List<Post> posts = postRepo.getPostsByUserId(id); // Get posts of the user
        List<Post> allPosts = postRepo.findAllByOrderByCreatedAt();
        String baseUrl = "localhost:8080";
        allPosts.forEach(post -> {
            post.setTimeAgo(TimeAgoUtil.toTimeAgo(post.getCreatedAt()));
            String fullUrl = baseUrl + "/post/" + post.getId();
            post.setPostUrl(fullUrl);
            post.setPostTags(post.getPostTags());
        });
        Post post = new Post();
        String email = user.getEmail();
        System.out.println(email);
        model.addAttribute("user", user);
        model.addAttribute("posts", posts);
        model.addAttribute("allPosts", allPosts);
        model.addAttribute("post", post);
        model.addAttribute("email", email);
        return "dashboard";
    }

    @GetMapping("/view/{id}")
    public String userView(Model model, @PathVariable("id") int userId) {
        User user = userService.findById(userId);
        model.addAttribute(user);
        List<Post> postsByUser = postRepo.getPostsByUserId(userId);
        model.addAttribute("postsByUser",postsByUser);
        return "userDetails";
    }

    @GetMapping("/search")
    public String searchUsers(@RequestParam("searchName") String searchName, Model model) {
        List<User> users = userService.searchUsersByName(searchName);
        model.addAttribute("users", users);
        model.addAttribute("searchTerm", searchName);
        return "User_search_results"; // corresponds to user_search_results.html
    }
}