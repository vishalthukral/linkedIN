package com.linkedin_clone_application.repository;

import com.linkedin_clone_application.model.Comment;
import com.linkedin_clone_application.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findByPostId(int postId);
    List<Comment> findByPostOrderByCreatedAtDesc(Post post);
}