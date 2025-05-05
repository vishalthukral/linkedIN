package com.linkedin_clone_application.service;

import com.linkedin_clone_application.model.Comment;
import com.linkedin_clone_application.model.Post;
import com.linkedin_clone_application.model.User;
import com.linkedin_clone_application.repository.CommentRepository;
import com.linkedin_clone_application.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {
    private CommentRepository commentRepository;
    private PostRepository postRepository;


    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }


    public void addComment(String content, Post post, User user) {
        Comment comment = new Comment();
        comment.setCommentContent(content);
        comment.setPost(post);
        comment.setUser(user);
        comment.setCreatedAt(LocalDateTime.now());
        commentRepository.save(comment);
    }


    @Override
    public Comment getCommentById(int commentId) {
        Optional<Comment> result = commentRepository.findById(commentId);
        Comment comment = null;
        if (result.isPresent()) {
            comment = result.get();
        } else {
            // we didn't find the Comment
            throw new RuntimeException("Did not find Comment - " + commentId);
        }
        return comment;
    }

    @Override
    public void deleteCommentById(int commentId) {
        commentRepository.deleteById(commentId);
    }

    public List<Comment> getCommentsByPostId(int postId) {
        return commentRepository.findByPostId(postId);
    }

    public List<Comment> getCommentsByPost(Post post) {
        return commentRepository.findByPostOrderByCreatedAtDesc(post);
    }

    public int getPostIdByCommentId(int commentId) {
        Comment comment = getCommentById(commentId);
        return comment.getPost().getId();
    }
    public void updateComment(int id, String content) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found with id: " + id));

        comment.setCommentContent(content);
        commentRepository.save(comment);
    }
}