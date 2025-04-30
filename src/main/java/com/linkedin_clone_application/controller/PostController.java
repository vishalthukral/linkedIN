package com.linkedin_clone_application.controller;

import com.cloudinary.Cloudinary;
import com.linkedin_clone_application.model.Post;
import com.linkedin_clone_application.repository.PostRepo;
import com.linkedin_clone_application.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Controller
public class PostController {
    private final PostService postService;
    private final PostRepo postRepo;
    PostController(PostService postService, PostRepo postRepo){
        this.postService = postService;
        this.postRepo = postRepo;
    }

    @GetMapping("/createpost")
    public String createPost(){
        return "createpostform";
    }

    @PostMapping("/savepost")
    public String savePost(@ModelAttribute Post post, MultipartFile[] multipartFiles, Cloudinary cloudinary){
        postRepo.save(post);
        return "redirect:/";
    }

    @PostMapping("/deletepost")
    @Transactional
    public String deletePost(@RequestParam int id){
        postService.deletePostById(id);
        return "redirect:/";
    }

    @GetMapping("/")
    public String getAllPosts(Model model){
        List<Post> postList = postService.getAllPost();
        model.addAttribute("allposts",postList);
        return "showallposts";
    }

    @GetMapping("/updateform")
    @Transactional
    public String updatePost(){
        return "createpostform";
    }

    @PostMapping("/updatepost")
    @Transactional
    public String updatePost(@ModelAttribute int id){
        return "redirect:/";
    }
}