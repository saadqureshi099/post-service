package com.instagram.Post.dto;


import lombok.Data;

@Data
public class PostCommentDto {
    private String content;
    private long userId;
    private long postId;

}
