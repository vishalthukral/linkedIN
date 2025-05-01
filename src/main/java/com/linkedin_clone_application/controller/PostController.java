package com.linkedin_clone_application.controller;

import com.cloudinary.Cloudinary;
import com.linkedin_clone_application.model.Media;
import com.linkedin_clone_application.model.Post;
import com.linkedin_clone_application.model.User;
import com.linkedin_clone_application.repository.PostRepo;
import com.linkedin_clone_application.repository.UserRepo;
import com.linkedin_clone_application.service.CloudinaryService;
import com.linkedin_clone_application.service.PostService;
import com.linkedin_clone_application.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class PostController {
    private final PostService postService;
    private final PostRepo postRepo;
    private final CloudinaryService cloudinaryService;
    private final UserRepo userRepo;
    private final UserService userService;

    PostController(PostService postService, PostRepo postRepo, CloudinaryService cloudinaryService, UserRepo userRepo, UserService userService){
        this.postService = postService;
        this.postRepo = postRepo;
        this.cloudinaryService = cloudinaryService;
        this.userRepo = userRepo;

        this.userService = userService;
    }

    @GetMapping("/createpost")
    public String createPost(Model model){
        String email = getLoggedInUserEmail();
        if(email!=null){
            User user = userService.findByEmail(email);
            Post post = new Post();
            post.setUser(user);
            model.addAttribute("post", post);
            model.addAttribute("email",email);
        }
        return "createPostForm";
    }

    @PostMapping("/savepost")
    public String savePost(@ModelAttribute Post post, @RequestParam("image") MultipartFile image) throws IOException {
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());
        if (image != null && !image.isEmpty()) {
            String imageUrl = cloudinaryService.uploadImage(image);
            Media media = new Media();
            media.setUrl(imageUrl);
            media.setPost(post);
            post.setMediaFile(media);
        }

        String email = getLoggedInUserEmail();
        System.out.println(email);
        if (email!=null){
            User user=userService.findByEmail(email);
            post.setUser(user);
        }

        postRepo.save(post);
        //return "redirect:/dashboard/1"+post.getUser().getId();
        return "redirect:/dashboard/1";
    }

    @PostMapping("/deletepost/{id}")
    @Transactional
    public String deletePost(@PathVariable int id){
        Post post = postRepo.findById(id).orElseThrow();
        postService.deletePostById(id);
        return "redirect:/dashboard/"+post.getUser().getId();
    }

//    @GetMapping("/")
//    public String getAllPosts(Model model){
//        List<Post> postList = postService.getAllPost();
//        model.addAttribute("allposts",postList);
//        return "showallposts";
//    }


    @GetMapping("/updateform/{id}")
    @Transactional
    public String updatePostForm(@PathVariable int id, Model model){
        Post post = postRepo.findById(id).orElseThrow();
        model.addAttribute("post", post);
        return "createPostForm";
    }

    @PostMapping("/updatepost")
    @Transactional
    public String updatePost(@ModelAttribute Post post){
        post.setUpdatedAt(LocalDateTime.now());
        postRepo.save(post);
        return "redirect:/dashboard/"+post.getUser().getId();
    }

    private String getLoggedInUserEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) auth.getPrincipal()).getUsername(); // this returns email
        }
        return null;
    }
}