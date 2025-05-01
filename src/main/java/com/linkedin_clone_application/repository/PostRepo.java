package com.linkedin_clone_application.repository;

import com.linkedin_clone_application.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepo extends JpaRepository<Post,Integer> {
    @Query("SELECT p FROM Post p ORDER BY p.createdAt DESC")
    List<Post> findAllByOrderByCreatedAt();

    List<Post> findByContentContainingIgnoreCase(String content);

    List<Post> getPostsByUserId(int id);
}