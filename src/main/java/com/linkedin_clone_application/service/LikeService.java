package com.linkedin_clone_application.service;

import com.linkedin_clone_application.model.Like;
import com.linkedin_clone_application.model.Post;
import com.linkedin_clone_application.model.User;
import com.linkedin_clone_application.repository.LikeRepository;
import com.linkedin_clone_application.repository.PostRepository;
import com.linkedin_clone_application.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LikeService {
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public LikeService(LikeRepository likeRepository, PostRepository postRepository, UserRepository userRepository) {
        this.likeRepository = likeRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    // Toggle like status (like or remove like)
    public void toggleLike(int postId, int userId) {
        Post post = postRepository.findById(postId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();

        Optional<Like> existing = likeRepository.findByPostAndUser(post, user);

        if (existing.isPresent()) {
            likeRepository.delete(existing.get());
            post.setLikesCount(post.getLikesCount() - 1);
        } else {
            Like like = new Like();
            like.setPost(post);
            like.setUser(user);
            likeRepository.save(like);
            post.setLikesCount(post.getLikesCount() + 1);
        }

        postRepository.save(post); // only save likeCount update, not the full post
    }

    public int getLikeCount(Post post) {
        return likeRepository.countByPost(post);
    }
}