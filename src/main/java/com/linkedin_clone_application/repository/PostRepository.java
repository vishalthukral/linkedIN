package com.linkedin_clone_application.repository;

import com.linkedin_clone_application.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {
    @Query("SELECT p FROM Post p ORDER BY p.createdAt DESC")
    List<Post> findAllByOrderByCreatedAt();

    List<Post> findByContentContainingIgnoreCase(String content);

    List<Post> getPostsByUserId(int id);

    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.comments")
    List<Post> findAllWithComments();

}