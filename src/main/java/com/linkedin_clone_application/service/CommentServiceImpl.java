package com.linkedin_clone_application.service;

import com.linkedin_clone_application.model.Comment;
import com.linkedin_clone_application.model.Post;
import com.linkedin_clone_application.repository.CommentRepo;
import com.linkedin_clone_application.repository.PostRepo;
import com.linkedin_clone_application.service.CommentService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {
    private CommentRepo commentRepository;
    private PostRepo postRepository;

    public CommentServiceImpl(CommentRepo  commentRepository,PostRepo  postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository=postRepository;
    }

    @Override
    @Transactional
    public void createComment(Comment comment, int postId) {
        //get the post
        Optional<Post> optionalPost = postRepository.findById(postId);

        //no post available with this id then throw exception
        if(optionalPost.isEmpty()){
            throw new RuntimeException("Post ID not found: " + postId);
        }

        Post post = optionalPost.get();

        //add all other fields that are necessary
        comment.setCreatedAt(LocalDateTime.now());
        comment.setPost(post);
        //check the request is for new or update
        if(comment.getId() == 0){
            comment.setUpdatedAt(null);
        }else {
            comment.setUpdatedAt(LocalDateTime.now());
        }

        commentRepository.save(comment);
    }

    @Override
    public Comment saveComment(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public Comment getCommentById(int commentId) {
        Optional<Comment> result = commentRepository.findById(commentId);
        Comment comment =null;
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
}