package com.instagram.Post.dto;

import lombok.Data;

import java.util.List;

@Data
public class PostListDto {
    private Long userId;
    private String content;
    private List<PostImageDto> images;
    private List<PostCommentDto> comments;
}
