package com.linkedin_clone_application.service;

import com.linkedin_clone_application.model.Comment;
import com.linkedin_clone_application.model.Post;
import com.linkedin_clone_application.model.User;

import java.util.List;

public interface CommentService {
    void addComment(String content, Post post, User user);

    List<Comment> getCommentsByPost(Post post);
    Comment getCommentById(int commentId);

    void deleteCommentById(int commentId);
    int getPostIdByCommentId(int commentId);
    void updateComment(int id, String content);
    int getCommentCountByPost(Post post);
}