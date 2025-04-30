package com.linkedin_clone_application.service;

import com.linkedin_clone_application.model.Comment;

public interface CommentService {
    void createComment(Comment comment, int postId);

    Comment saveComment(Comment comment);

    Comment getCommentById(int commentId);

    void deleteCommentById(int commentId);
}