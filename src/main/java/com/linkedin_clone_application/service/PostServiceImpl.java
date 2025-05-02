package com.linkedin_clone_application.service;

import com.linkedin_clone_application.enums.MediaType;
import com.linkedin_clone_application.model.Media;
import com.linkedin_clone_application.model.Post;
import com.linkedin_clone_application.repository.MediaRepo;
import com.linkedin_clone_application.repository.PostRepo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {
    private PostRepo postRepo;
    private MediaRepo mediaRepo;
    private CloudinaryService cloudinaryService;

    public PostServiceImpl(PostRepo postRepo, MediaRepo mediaRepo, CloudinaryService cloudinaryService) {
        this.postRepo = postRepo;
        this.mediaRepo = mediaRepo;
        this.cloudinaryService = cloudinaryService;
    }

    @Override
    public String savePost(Post post, MultipartFile[] mediaFiles, MediaType type) {
        // save Post
        post = postRepo.save(post);

        List<Media> uploadedMedia = new ArrayList<>();
        System.out.println("uploaded media : " + uploadedMedia);
        if (mediaFiles != null && mediaFiles.length > 0) {
            for (MultipartFile file : mediaFiles) {
                if (file != null && !file.isEmpty()) {
                    String url = cloudinaryService.uploadFile(file);
                    if (url != null) {
                        Media media = new Media();
                        media.setPost(post);
                        media.setUrl(url);
                        media.setType(type);
                        media = mediaRepo.save(media);
                        uploadedMedia.add(media);
                    }
                }
            }
        }
        return "hi";
    }

    @Override
    public List<Post> getAllPost() {
        return postRepo.findAll();
    }

    public List<Post> searchPosts(String content) {
        List<Post> list = postRepo.findByContentContainingIgnoreCase(content);
        System.out.println("listtttttt" + list.toString());
        return list;
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

    @Override
    public List<Post> getPostsByUserId(int id) {
        return postRepo.getPostsByUserId(id);
    }

    public List<Post> getAllPostsWithComments() {
        return postRepo.findAllWithComments();
    }
}