package com.linkedin_clone_application.controller;

import com.linkedin_clone_application.Util.TimeAgoUtil;
import com.linkedin_clone_application.model.*;
import com.linkedin_clone_application.repository.ArticleRepository;
import com.linkedin_clone_application.repository.PostRepository;
import com.linkedin_clone_application.repository.TagRepository;
import com.linkedin_clone_application.repository.UserRepository;
import com.linkedin_clone_application.service.*;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ArticleController {
    private final PostService postService;
    private final ArticleRepository articleRepository;
    private final CloudinaryService cloudinaryService;
    private final UserRepository userRepository;
    private final UserService userService;
    private final LikeService likeService;
    private final CommentService commentService;
    private final PostTagService postTagService;
    private final TagRepository tagRepository;

    ArticleController(PostService postService,ArticleRepository articleRepository, CloudinaryService cloudinaryService,
                      UserRepository userRepository,
                      UserService userService,
                      LikeService likeService, CommentService commentService, PostTagService postTagService,
                      TagRepository tagRepository) {
        this.postService = postService;
        this.articleRepository = articleRepository;
        this.cloudinaryService = cloudinaryService;
        this.userRepository = userRepository;
        this.userService = userService;
        this.likeService = likeService;
        this.commentService = commentService;
        this.postTagService = postTagService;
        this.tagRepository = tagRepository;
    }

    @GetMapping("/createArticle")
    public String createArticle(Model model) {
        String email = getLoggedInUserEmail();
        if (email != null) {
            User user = userService.findByEmail(email);
            Article article = new Article();
            article.setUser(user);
            model.addAttribute("article", article);
            model.addAttribute("email", email);
        } else {
            return "redirect:/login";
        }
        return "createArticle";
    }

    @PostMapping("/saveArticle")
    public String saveArticle(@Valid @ModelAttribute Article article, BindingResult result){

        if (result.hasErrors()) {
            return "createArticle";
        }
        if (article.getId() == 0) {
            article.setCreatedAt(LocalDateTime.now()); // Only set createdAt if it's a new article
        }
        article.setUpdatedAt(LocalDateTime.now());

        String email = getLoggedInUserEmail();
        System.out.println(email);
        if (email != null) {
            User user = userService.findByEmail(email);
            article.setUser(user);
        }
        articleRepository.save(article);

        return "redirect:/dashboard/" + article.getUser().getId();
    }

    @GetMapping("/articleDelete/{id}")
    @Transactional
    public String deleteArticle(@PathVariable int id) {
        Article article = articleRepository.findById(id).get();
        articleRepository.deleteById(id);
        return "redirect:/view/" + article.getUser().getId() + "?detail=article";
    }

    @GetMapping("/articleUpdate/{id}")
    public String updateArticleForm(@PathVariable int id, Model model) {
        Article article = articleRepository.findById(id).get();

        model.addAttribute("article", article);
        return "createArticle";
    }
//
//    @PostMapping("/toggle/{postId}")
//    public String likePost(@PathVariable int postId) {
//        // Get the logged-in user's email
//        String email = getLoggedInUserEmail();
//        if (email == null) {
//            return "redirect:/login";
//        }
//
//        User user = userService.findByEmail(email);
//        likeService.toggleLike(postId, user.getId());
//
//        return "redirect:/dashboard/" + user.getId();
//    }
//
    @GetMapping("/article/{id}")
    public String viewPost(@PathVariable("id") int articleId, Model model) {
        Article article = articleRepository.findById(articleId).get();
        String email = getLoggedInUserEmail();
        if (email == null) {
            return "redirect:/login";
        }
        if (article == null) {
            return "redirect:/dashboard";
        }

        User currentUser = userService.findByEmail(email);
        if (currentUser != null) {
            model.addAttribute("user", currentUser);
        }

        model.addAttribute("article", article);
        model.addAttribute("email", email);
        return "article";  // This should match your Thymeleaf template name
    }
//
//    @PostMapping("/post/{id}/comment")
//    public String addComment(@PathVariable("id") int postId,
//                             @RequestParam("content") String content) {
//        String email = getLoggedInUserEmail();
//        if (email == null) {
//            return "redirect:/login";
//        }
//
//        User user = userService.findByEmail(email);
//        Post post = postService.getPostById(postId);
//
//
//        commentService.addComment(content, post, user);
//
//        return "redirect:/dashboard/" + user.getId();
//    }
//
//    @PostMapping("/repost/{id}")
//    public String repostPost(@PathVariable("id") int postId, RedirectAttributes redirectAttributes) {
//        String email = getLoggedInUserEmail();
//        if (email == null) {
//            return "redirect:/login";
//        }
//
//        User user = userService.findByEmail(email);
//        Post originalPost = postService.getPostById(postId);
//
//        Post repost = new Post();
//        repost.setUser(originalPost.getUser());
//        repost.setOriginalPost(originalPost);
//        repost.setTitle(originalPost.getTitle());
//        repost.setContent(originalPost.getContent());
//        repost.setCreatedAt(LocalDateTime.now());
//        repost.setUpdatedAt(LocalDateTime.now());
//        repost.setRepostedBy(user);
//
//        Media originalMedia = originalPost.getMediaFile();
//        if (originalMedia != null) {
//            Media repostMedia = new Media();
//            repostMedia.setUrl(originalMedia.getUrl());
//            repostMedia.setPost(repost);
//            repost.setMediaFile(repostMedia);
//        }
//
//        postService.savePost(repost, null, null);
//
//        redirectAttributes.addFlashAttribute("repostSuccess",
//                "This post is reposted successfully!");
//        return "redirect:/dashboard/" + user.getId();
//    }

    private String getLoggedInUserEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) auth.getPrincipal()).getUsername();
        }
        return null;
    }
}