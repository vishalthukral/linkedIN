package com.linkedin_clone_application.service;

import com.linkedin_clone_application.model.Like;
import com.linkedin_clone_application.model.Post;
import com.linkedin_clone_application.model.User;
import com.linkedin_clone_application.repository.LikeRepo;
import com.linkedin_clone_application.repository.PostRepo;
import com.linkedin_clone_application.repository.UserRepo;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LikeService {

    private final LikeRepo likeRepo;
    private final PostRepo postRepo;
    private final UserRepo userRepo;

    public LikeService(LikeRepo likeRepo, PostRepo postRepo, UserRepo userRepo) {
        this.likeRepo = likeRepo;
        this.postRepo = postRepo;
        this.userRepo = userRepo;
    }

    // Toggle like status (like or remove like)
    public void toggleLike(int postId, int userId) {
        Post post = postRepo.findById(postId).orElseThrow();
        User user = userRepo.findById(userId).orElseThrow();

        Optional<Like> existing = likeRepo.findByPostAndUser(post, user);

        if (existing.isPresent()) {
            likeRepo.delete(existing.get());
            post.setLikesCount(post.getLikesCount() - 1);
        } else {
            Like like = new Like();
            like.setPost(post);
            like.setUser(user);
            likeRepo.save(like);
            post.setLikesCount(post.getLikesCount() + 1);
        }

        postRepo.save(post); // only save likeCount update, not the full post
    }


    // Get total likes for a post
    public int getLikeCount(Post post) {
        return likeRepo.countByPost(post);
    }
}