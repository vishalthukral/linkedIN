package com.linkedin_clone_application.service;

import com.linkedin_clone_application.enums.MediaType;
import com.linkedin_clone_application.model.Post;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface PostService {
    String savePost(Post post, MultipartFile[] mediafiles, MediaType mediaType);

    List<Post> getAllPost();

    void deletePostById(int id);

    Post getPostById(int id);

    List<Post> getPostsByUserId(int id);


}