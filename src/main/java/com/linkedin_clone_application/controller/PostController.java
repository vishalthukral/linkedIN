package com.linkedin_clone_application.controller;

import com.linkedin_clone_application.Util.TimeAgoUtil;
import com.linkedin_clone_application.model.Comment;
import com.linkedin_clone_application.model.Media;
import com.linkedin_clone_application.model.Post;
import com.linkedin_clone_application.model.User;
import com.linkedin_clone_application.repository.PostRepo;
import com.linkedin_clone_application.repository.UserRepo;
import com.linkedin_clone_application.service.*;
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
    private final LikeService likeService;
    private final CommentService commentService;

    PostController(PostService postService, PostRepo postRepo, CloudinaryService cloudinaryService, UserRepo userRepo, UserService userService,
                   LikeService likeService, CommentService commentService){
        this.postService = postService;
        this.postRepo = postRepo;
        this.cloudinaryService = cloudinaryService;
        this.userRepo = userRepo;

        this.userService = userService;
        this.likeService = likeService;
        this.commentService = commentService;
    }

    @GetMapping("/createPost")
    public String createPost(Model model){
        String email = getLoggedInUserEmail();
        if(email!=null){
            User user = userService.findByEmail(email);
            Post post = new Post();
            post.setUser(user);
            model.addAttribute("post", post);
            model.addAttribute("email",email);
        }else{
            return "redirect:/login";
        }
        return "createPost";
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
        return "redirect:/dashboard/"+post.getUser().getId();
    }

    @PostMapping("/deletepost/{id}")
    @Transactional
    public String deletePost(@PathVariable int id){
        Post post = postService.getPostById(id);
        postService.deletePostById(id);
        return "redirect:/dashboard/"+post.getUser().getId();
    }

    @GetMapping("/")
    public String getAllPosts(Model model){
        return "redirect:/login";
    }
    @GetMapping("/updateform/{id}")
    @Transactional
    public String updatePostForm(@PathVariable int id, Model model){
        Post post = postService.getPostById(id);
        model.addAttribute("posting", post);
        return "createPostForm";
    }

@PostMapping("/toggle/{postId}")
public String likePost(@PathVariable int postId) {
    // Get the logged-in user's email
    String email = getLoggedInUserEmail();
    if (email == null) {
        return "redirect:/login";  // If not logged in, redirect to login
    }

    User user = userService.findByEmail(email); // Find the user by email
    likeService.toggleLike(postId, user.getId());  // Toggle like for the post

    return "redirect:/dashboard/" + user.getId();  // Redirect back to user's dashboard
}

    @GetMapping("/post/{id}")
    public String viewPost(@PathVariable("id") int postId, Model model) {
        Post post = postService.getPostById(postId);
        List<Comment> comments = commentService.getCommentsByPost(post);
post.setTimeAgo(TimeAgoUtil.toTimeAgo(post.getCreatedAt()));
        model.addAttribute("post", post);
        model.addAttribute("comments", comments);
//        System.out.println(comments.get(0).getCommentContent());
        return "postDetails";  // This should match your Thymeleaf template name
    }
    @PostMapping("/post/{id}/comment")
    public String addComment(@PathVariable("id") int postId,
                             @RequestParam("content") String content) {
        String email = getLoggedInUserEmail();
        if (email == null) {
            return "redirect:/login";  // If not logged in, redirect to login
        }

        User user = userService.findByEmail(email);  // Get the logged-in user
        Post post = postService.getPostById(postId);


        commentService.addComment(content, post, user);  // Use your service method

        return "redirect:/dashboard/"+post.getUser().getId();  // Redirect back to the post details page
    }

    private String getLoggedInUserEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) auth.getPrincipal()).getUsername(); // this returns email
        }
        return null;
    }
}