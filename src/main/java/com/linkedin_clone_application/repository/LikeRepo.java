package com.linkedin_clone_application.repository;

import com.linkedin_clone_application.model.Like;
import com.linkedin_clone_application.model.Post;
import com.linkedin_clone_application.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepo extends JpaRepository<Like,Integer> {
    boolean existsByPostAndUser(Post post, User user);
    void deleteByPostAndUser(Post post, User user);
    int countByPost(Post post);
}