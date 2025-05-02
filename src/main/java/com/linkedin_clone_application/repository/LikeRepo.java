package com.linkedin_clone_application.repository;

import com.linkedin_clone_application.model.Like;
import com.linkedin_clone_application.model.Post;
import com.linkedin_clone_application.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepo extends JpaRepository<Like, Integer> {
    boolean existsByPostAndUser(Post post, User user);  // Check if user has liked the post

    void deleteByPostAndUser(Post post, User user);    // Remove like by user and post

    int countByPost(Post post);

    int countByPostId(int postId);

    Optional<Like> findByPostAndUser(Post post, User user);
}