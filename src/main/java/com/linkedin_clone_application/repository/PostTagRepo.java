package com.linkedin_clone_application.repository;


import com.linkedin_clone_application.model.Post;
import com.linkedin_clone_application.model.PostTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostTagRepo extends JpaRepository<PostTag, Integer> {
    PostTag findByPostIdAndTagId(int postId, int tagId);


    @Query("SELECT pt.tag.name FROM PostTag pt WHERE pt.post.id = :postId")
    List<String> findTagsByPostId(@Param("postId") int postId);

    void deleteByPostId(int postId);

    void deleteAllByPost(Post existingPost);

    List<PostTag> findByPostId(Long postId);
}