package com.linkedin_clone_application.service;

import com.linkedin_clone_application.model.Like;
import com.linkedin_clone_application.model.Post;
import com.linkedin_clone_application.model.User;
import com.linkedin_clone_application.repository.LikeRepo;
import com.linkedin_clone_application.repository.PostRepo;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LikeService {

    private final LikeRepo likeRepo;
    private final PostRepo postRepo;

    public LikeService(LikeRepo likeRepo, PostRepo postRepo) {
        this.likeRepo = likeRepo;
        this.postRepo = postRepo;
    }

    // Toggle like status (like or remove like)
    public void toggleLike(int postId, int userId) {
        Post post = new Post();  // Retrieve the post by its ID
        post.setId(postId);

        User user = new User();  // Retrieve the user by its ID
        user.setId(userId);

        // Check if the user has already liked the post

        Optional<Like> liked=likeRepo.findByPostAndUser(post,user);

        if (liked.isPresent()) {
            // If the user has already liked the post, remove the like
            likeRepo.delete(liked.get());
            post.setLikesCount(post.getLikesCount() -1);
        } else {
            // Otherwise, add the like
            Like like = new Like();
            like.setPost(post);
            like.setUser(user);
            likeRepo.save(like);
            post.setLikesCount(post.getLikesCount() + 1);
        }
        postRepo.save(post);
    }

    // Get total likes for a post
    public int getLikeCount(Post post) {
        return likeRepo.countByPost(post);
    }
}