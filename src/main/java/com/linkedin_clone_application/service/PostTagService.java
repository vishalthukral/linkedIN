package com.linkedin_clone_application.service;



import com.linkedin_clone_application.model.Post;
import com.linkedin_clone_application.model.PostTag;
import com.linkedin_clone_application.model.Tag;

import java.util.List;

public interface PostTagService {
    void save(PostTag postTag);
    PostTag findByPostIdAndTagId(int postId,int tagId);

    public List<String> findTagsByPostID(int postId);
    void deleteByPostId(int id);
    void createPostTag(Tag tag, Post post);
}