package com.instagram.Post.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostImageDto {
    private String imageUrl;
}
