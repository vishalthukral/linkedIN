package com.linkedin_clone_application.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = true)
    private LocalDateTime updatedAt;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

   @OneToMany(mappedBy = "post",cascade = CascadeType.ALL,orphanRemoval = true)
   private List<Media> mediaFile;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Comment> comments;

    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Like> likes;

    private int likesCount;
    private int CommentCount;
    private int sharesCount;

    @PrePersist
    public void prePersist() {
        // Ensure the createdAt field is set only when the entity is first persisted
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        // Always set the updatedAt field whenever the entity is persisted or updated
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}