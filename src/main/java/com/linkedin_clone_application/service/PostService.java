package com.linkedin_clone_application.service;

import com.linkedin_clone_application.model.Post;

import java.util.List;

public interface PostService {
    Post savePost(Post post);
    List<Post> getAllPost();
    void deletePostById(int id);
    Post getPostById(int id);
}