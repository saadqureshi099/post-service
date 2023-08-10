package com.instagram.Post.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PostDto {
    private Long userId;
    private String content;
    private List<PostImageDto> images;

}
