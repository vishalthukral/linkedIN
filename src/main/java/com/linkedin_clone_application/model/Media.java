package com.linkedin_clone_application.model;

import com.linkedin_clone_application.enums.MediaType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
//@Getter
//@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "media")
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    private String url;  // Store Cloudinary URL

    @Enumerated(EnumType.STRING)
    private MediaType type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public MediaType getType() {
        return type;
    }

    public void setType(MediaType type) {
        this.type = type;
    }
}