package com.linkedin_clone_application.repository;

import com.linkedin_clone_application.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepo extends JpaRepository<Comment, Integer> {
}