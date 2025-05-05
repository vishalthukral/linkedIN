package com.linkedin_clone_application.repository;

import com.linkedin_clone_application.model.Media;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MediaRepository extends JpaRepository<Media, Integer> {
    List<Media> findByPostId(int postId);
}