package com.instagram.Post.service;

import com.instagram.Post.dto.AddImageDto;
import com.instagram.Post.model.Post;
import com.instagram.Post.model.PostImage;
import com.instagram.Post.repository.PostImageRepository;
import com.instagram.Post.repository.PostRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostImageService {
    private final PostImageRepository postImageRepository;
    private final PostRepository postRepository;

    /**
     * Persists a Image for a post
     * @param postId
     * @param postImageDto
     * @return
     */
    public PostImage imagePost(long postId, AddImageDto postImageDto){
        Post post = postRepository.findById(postId).orElseThrow();
        PostImage postImage = PostImage.builder()
                .post(post)
                .imageUrl(postImageDto.getImageUrl())
                .build();

        return postImageRepository.save(postImage);
    }
}
