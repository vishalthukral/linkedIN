package com.linkedin_clone_application.repository;

import com.linkedin_clone_application.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepo extends JpaRepository<Post,Integer> {
}