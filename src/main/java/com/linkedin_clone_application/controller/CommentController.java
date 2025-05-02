package com.linkedin_clone_application.controller;

import com.linkedin_clone_application.model.Comment;
import com.linkedin_clone_application.model.Post;
import com.linkedin_clone_application.model.User;
import com.linkedin_clone_application.service.CommentService;
import com.linkedin_clone_application.service.PostService;
import com.linkedin_clone_application.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Controller
@RequestMapping("comments")
public class CommentController {
    private PostService postService;
    private UserService userService;
    private CommentService commentService;

    public CommentController(PostService postService, UserService userService, CommentService commentService) {
        this.postService = postService;
        this.userService = userService;
        this.commentService = commentService;
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable int id, Model model) {
        Comment comment = commentService.getCommentById(id);
        model.addAttribute("comment", comment);
        return "comment-update"; // renamed to use 'update'
    }
    @PostMapping("/update/{id}")
    public String updateComment(@PathVariable int id, @RequestParam String content) {
        commentService.updateComment(id, content);
        int postId = commentService.getPostIdByCommentId(id);
        return "redirect:/post/"+postId;
    }
    @PostMapping("/delete/{id}")
    public String deleteComment(@PathVariable int id) {
        int postId = commentService.getPostIdByCommentId(id);
        commentService.deleteCommentById(id);
        return "redirect:/post/" + postId;
    }

    private String getLoggedInUserEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) auth.getPrincipal()).getUsername(); // this returns email
        }
        return null;
    }
}