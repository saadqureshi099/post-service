package com.instagram.Post.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "post_img_tbl")
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class PostImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "post_id")
    @JsonBackReference
    private Post post;
    public PostImage(String imageUrl, Post post){
        this.imageUrl = imageUrl;
        this.post = post;
    }

}