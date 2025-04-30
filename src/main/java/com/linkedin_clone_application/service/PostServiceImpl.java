package com.linkedin_clone_application.service;

import com.linkedin_clone_application.model.Post;
import com.linkedin_clone_application.repository.PostRepo;

import java.util.List;
import java.util.Optional;

public class PostServiceImpl implements PostService {
    private PostRepo postRepo;

    public PostServiceImpl(PostRepo postRepo) {
        this.postRepo = postRepo;
    }

    @Override
    public Post savePost(Post post) {
       return postRepo.save(post);
    }

    @Override
    public List<Post> getAllPost() {
        return postRepo.findAll();
    }

    @Override
    public void deletePostById(int id) {
        postRepo.deleteById(id);
    }

    @Override
    public Post getPostById(int id) {
        Optional<Post> post = postRepo.findById(id);
        Post result = null;
        if (post.isPresent()) {
            result = post.get();
        } else {
            throw new RuntimeException("Post doesn't exist");
        }
        return result;
    }
}