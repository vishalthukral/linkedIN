package com.linkedin_clone_application.model;

import com.linkedin_clone_application.enums.MediaType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
@Getter
@Setter
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

}