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
    private final ArticleRepository articleRepository;
    private final UserService userService;

    ArticleController(ArticleRepository articleRepository, UserService userService) {
        this.articleRepository = articleRepository;
        this.userService = userService;
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
            article.setCreatedAt(LocalDateTime.now());
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
        return "article";
    }

    private String getLoggedInUserEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) auth.getPrincipal()).getUsername();
        }
        return null;
    }
}