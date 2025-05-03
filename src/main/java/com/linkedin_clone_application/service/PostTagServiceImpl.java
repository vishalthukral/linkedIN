package com.linkedin_clone_application.service;

import com.linkedin_clone_application.model.Post;
import com.linkedin_clone_application.model.PostTag;
import com.linkedin_clone_application.model.Tag;
import com.linkedin_clone_application.repository.PostTagRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostTagServiceImpl implements PostTagService {
    private PostTagRepo postTagRepository;

    @Autowired
    public PostTagServiceImpl(PostTagRepo postTagRepository) {
        this.postTagRepository = postTagRepository;
    }

    @Override
    @Transactional
    public void save(PostTag postTag) {
        postTagRepository.save(postTag);
    }

    @Override
    @Transactional
    public PostTag findByPostIdAndTagId(int postId, int tagId) {
        return postTagRepository.findByPostIdAndTagId(postId, tagId);
    }

    @Override
    public List<String> findTagsByPostID(int postId) {
        return postTagRepository.findTagsByPostId(postId);
    }
    @Override
    @Transactional
    public void deleteByPostId(int postId) {
        postTagRepository.deleteByPostId(postId);
    }
    @Override
    @Transactional
    public void createPostTag(Tag tag, Post post) {
        //set the needed data in postTag object
        PostTag postTag = new PostTag();

        postTag.setTag(tag);
        postTag.setPost(post);
        postTag.setCreatedAt(LocalDateTime.now());
        postTag.setUpdatedAt(LocalDateTime.now());

        postTagRepository.save(postTag);
    }
}