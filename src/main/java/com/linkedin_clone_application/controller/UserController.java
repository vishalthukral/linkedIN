package com.linkedin_clone_application.controller;

import com.linkedin_clone_application.Util.TimeAgoUtil;
import com.linkedin_clone_application.model.Job;
import com.linkedin_clone_application.model.Post;
import com.linkedin_clone_application.model.User;
import com.linkedin_clone_application.repository.PostRepository;
import com.linkedin_clone_application.service.CloudinaryService;
import com.linkedin_clone_application.service.ConnectionService;
import com.linkedin_clone_application.service.JobService;
import com.linkedin_clone_application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
public class UserController {
    private final UserService userService;
    private final CloudinaryService cloudinaryService;
    private final PostRepository postRepository;
    private final JobService jobService;
    private final ConnectionService connectionService;

    @Autowired
    public UserController(UserService userService, CloudinaryService cloudinaryService, PostRepository postRepository,
                          JobService jobService, ConnectionService connectionService) {
        this.userService = userService;
        this.cloudinaryService = cloudinaryService;
        this.postRepository = postRepository;
        this.jobService = jobService;
        this.connectionService = connectionService;
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
        return "completeProfile";
    }

    @PostMapping("/userDetails")
    public String userProfile(@ModelAttribute("user") User user, @RequestParam("image") MultipartFile image) throws IOException {
        if (image != null && !image.isEmpty()) {
            String imageUrl = cloudinaryService.uploadImage(image);
            user.setProfilePictureUrl(imageUrl);
        } else if (user.getProfilePictureUrl() == null || user.getProfilePictureUrl().isEmpty()) {
            user.setProfilePictureUrl("/images/default-profile.jpg");
        }
        userService.saveUser(user);
        int id = user.getId();
        return "redirect:/jobDetails/" + id;
    }

    @GetMapping("/jobDetails/{Id}")
    public String jobDetails(@PathVariable int Id, Model model) {
        User user = userService.findById(Id);
        model.addAttribute("user", user);
        return "enterJobDetails";
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
        List<Post> posts = postRepository.getPostsByUserId(id); // Get posts of the user
        List<Post> allPosts = postRepository.findAllByOrderByCreatedAt();
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
        String email = getLoggedInUserEmail();
        User currentUser = userService.findByEmail(email);
        if (email == null) {
            return "redirect:/login";
        }
        List<User> users = userService.findAllExcept(currentUser);
        Map<Integer, Boolean> connectionStatusMap = new HashMap<>();
        for (User u : users) {
            boolean alreadyRequested = connectionService.isRequestSentOrConnected(currentUser, u);
            connectionStatusMap.put(u.getId(), alreadyRequested);
        }
        User user = userService.findById(userId);
        List<Post> postsByUser = postRepository.getPostsByUserId(userId);
        model.addAttribute("user", user);
        model.addAttribute("postsByUser", postsByUser);
        model.addAttribute("email", email);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("connectionStatusMap", connectionStatusMap);
        return "userDetails";
    }

    @GetMapping("/search")
    public String searchUsers(@RequestParam("searchName") String searchName, Model model) {
        String email = getLoggedInUserEmail();
        if (email == null) {
            return "redirect:/login";
        }
        int userId = userService.findByEmail(email).getId();
        List<User> users = userService.searchUsersByName(searchName, userId);
        List<Job> jobsByTitleAndDescription = jobService.searchJobsByKeyword(searchName);
        model.addAttribute("users", users);

        model.addAttribute("searchTerm", searchName);
        model.addAttribute("jobs", jobsByTitleAndDescription);
        return "userSearchResults"; // corresponds to user_search_results.html
    }

    @GetMapping("/articles")
    public String articlesPage() {
        return "articles";
    }

    private String getLoggedInUserEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) auth.getPrincipal()).getUsername(); // this returns email
        }
        return null;
    }
}