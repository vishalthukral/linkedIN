package com.linkedin_clone_application.controller;

import com.linkedin_clone_application.Util.TimeAgoUtil;
import com.linkedin_clone_application.model.*;
import com.linkedin_clone_application.repository.PostRepository;
import com.linkedin_clone_application.repository.TagRepository;
import com.linkedin_clone_application.repository.UserRepository;
import com.linkedin_clone_application.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class PostController {
    private final PostService postService;
    private final PostRepository postRepository;
    private final CloudinaryService cloudinaryService;
    private final UserRepository userRepository;
    private final UserService userService;
    private final LikeService likeService;
    private final CommentService commentService;
    private final PostTagService postTagService;
    private final TagRepository tagRepository;

    PostController(PostService postService, PostRepository postRepository, CloudinaryService cloudinaryService, UserRepository userRepository,
                   UserService userService,
                   LikeService likeService, CommentService commentService, PostTagService postTagService, TagRepository tagRepository) {
        this.postService = postService;
        this.postRepository = postRepository;
        this.cloudinaryService = cloudinaryService;
        this.userRepository = userRepository;

        this.userService = userService;
        this.likeService = likeService;
        this.commentService = commentService;
        this.postTagService = postTagService;
        this.tagRepository = tagRepository;
    }

    @GetMapping("/createPost")
    public String createPost(Model model) {
        String email = getLoggedInUserEmail();
        if (email != null) {
            User user = userService.findByEmail(email);
            Post post = new Post();
            post.setUser(user);
            model.addAttribute("post", post);
            model.addAttribute("email", email);
        } else {
            return "redirect:/login";
        }
        return "createPost";
    }


    @PostMapping("/savepost")
    public String savePost(@ModelAttribute Post post, @RequestParam("tags") String tags, @RequestParam("image") MultipartFile image)
            throws IOException {
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());

        if (post.getId() != 0) {
            // Existing Post -> Update
            Post existingPost = postRepository.findById(post.getId())
                    .orElseThrow(() -> new RuntimeException("Post not found with id: " + post.getId()));
            post.setCreatedAt(existingPost.getCreatedAt());
            post.setUpdatedAt(LocalDateTime.now());  // set updatedAt

            // Delete old PostTags
            postTagService.deleteByPostId(post.getId());
        } else {
            post.setCreatedAt(LocalDateTime.now());
            post.setUpdatedAt(LocalDateTime.now());
        }
        Post savedPost = postRepository.save(post);
        String[] tagArray = tags.split(",");
        for (String tagName : tagArray) {
            final String trimmedTagName = tagName.trim();

            Tag tag = tagRepository.findByName(trimmedTagName)
                    .orElseGet(() -> {
                        Tag newTag = new Tag();
                        newTag.setName(trimmedTagName);
                        newTag.setCreatedAt(LocalDateTime.now());
                        newTag.setUpdatedAt(LocalDateTime.now());
                        return tagRepository.save(newTag);
                    });

            PostTag postTag = new PostTag();
            postTag.setPost(savedPost);
            postTag.setTag(tag);
            postTag.setCreatedAt(LocalDateTime.now());
            postTag.setUpdatedAt(LocalDateTime.now());

            postTagService.save(postTag);
        }

            if (image != null && !image.isEmpty()) {
                String imageUrl = cloudinaryService.uploadImage(image);
                Media media = new Media();
                media.setUrl(imageUrl);
                media.setPost(post);
                post.setMediaFile(media);
            }

        String email = getLoggedInUserEmail();
        System.out.println(email);
        if (email != null) {
            User user = userService.findByEmail(email);
            post.setUser(user);
        }
        postRepository.save(post);

        return "redirect:/dashboard/" + post.getUser().getId();
    }

    @PostMapping("/deletepost/{id}")
    @Transactional
    public String deletePost(@PathVariable int id) {
        Post post = postService.getPostById(id);
        postService.deletePostById(id);
        return "redirect:/dashboard/" + post.getUser().getId();
    }

    @GetMapping("/")
    public String getAllPosts(Model model) {
        return "redirect:/login";
    }

    @GetMapping("/updateform/{id}")
    public String updatePostForm(@PathVariable int id, Model model) {
        Post post = postService.getPostById(id);
        model.addAttribute("posting", post);
        return "updateForm";
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
    public String viewPost(@PathVariable("id") int postId, Model model, HttpSession session) {
        Post post = postService.getPostById(postId);

        if (post == null) {
            return "redirect:/dashboard";
        }

        User currentUser = (User) session.getAttribute("user");
        if (currentUser != null) {
            model.addAttribute("user", currentUser);
        }

        List<Comment> comments = commentService.getCommentsByPost(post);

        post.setCommentCount(comments.size());

        post.setTimeAgo(TimeAgoUtil.toTimeAgo(post.getCreatedAt()));

        for (Comment comment : comments) {
            comment.setTimeAgo(TimeAgoUtil.toTimeAgo(comment.getCreatedAt()));
        }

        model.addAttribute("post", post);
        model.addAttribute("comments", comments);
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

        return "redirect:/dashboard/" + post.getUser().getId();  // Redirect back to the post details page
    }

    @PostMapping("/repost/{id}")
    public String repostPost(@PathVariable("id") int postId, RedirectAttributes redirectAttributes) {
        String email = getLoggedInUserEmail();
        if (email == null) {
            return "redirect:/login";
        }

        User user = userService.findByEmail(email);
        Post originalPost = postService.getPostById(postId);

        Post repost = new Post();
        repost.setUser(originalPost.getUser());
        repost.setOriginalPost(originalPost);
        repost.setTitle(originalPost.getTitle());
        repost.setContent(originalPost.getContent());
        repost.setCreatedAt(LocalDateTime.now());
        repost.setUpdatedAt(LocalDateTime.now());
        repost.setRepostedBy(user);

        Media originalMedia = originalPost.getMediaFile();
        if (originalMedia != null) {
            Media repostMedia = new Media();
            repostMedia.setUrl(originalMedia.getUrl());
            repostMedia.setPost(repost);
            repost.setMediaFile(repostMedia);
        }

        postService.savePost(repost, null, null);

        redirectAttributes.addFlashAttribute("repostSuccess", "This post is reposted successfully!");
        return "redirect:/dashboard/" + user.getId();
    }

    private String getLoggedInUserEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) auth.getPrincipal()).getUsername(); // this returns email
        }
        return null;
    }
}