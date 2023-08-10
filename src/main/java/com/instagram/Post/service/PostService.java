package com.instagram.Post.service;

import com.instagram.Post.dto.*;
import com.instagram.Post.model.Post;
import com.instagram.Post.model.PostImage;
import com.instagram.Post.repository.PostImageRepository;
import com.instagram.Post.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@Slf4j

public class PostService {
    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final PostImageService postImageService;
    private final PostCommentService postCommentService;

    public PostService(PostRepository postRepository, PostImageRepository postImageRepository, PostImageService postImageService, PostCommentService postCommentService) {
        this.postRepository = postRepository;
        this.postImageRepository = postImageRepository;
        this.postImageService = postImageService;
        this.postCommentService = postCommentService;
    }


    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Optional<Post> getPostById(long id) {
        return postRepository.findById(id);
    }

    /**
     * Persisting a new Post
     * @param postdto
     */

    public void createPost(AddPostDto postdto) {
        Post post = Post.builder()
                .userId(postdto.getUserId())
                .content(postdto.getContent())
                .build();
        List<AddImageDto> images= postdto.getImages();
        Post savedPost = postRepository.save(post);

        images.forEach(i -> postImageService.imagePost(savedPost.getId(), i));

    }

    /**
     * Persisting new comment
     * @param commentDto
     */
    public void addComment(PostCommentDto commentDto){
        postCommentService.addCommentToPost(commentDto);
    }

    /**
     * Fetch posts of a specific user
     * @param userid
     * @return
     */
    public List<Post> getPostsByUserId(long userid) {
        return postRepository.findByUserId(userid);
    }

    /**
     * Updates User Post
     * @param id
     * @param updatedPost
     * @return
     * @throws ChangeSetPersister.NotFoundException
     */
    @Transactional
    public Post updatePost(long id, Post updatedPost) throws ChangeSetPersister.NotFoundException {
        Post existingPost = postRepository.findById(id)
                .orElseThrow(ChangeSetPersister.NotFoundException::new);


        /**
         * Delete existing image one by one
         * */
        Set<PostImage> existingImages = existingPost.getImages();
        existingImages.stream().forEach(postImageRepository::delete);

        /**
         * create and Associate new images
         * */
        Set<PostImage> newImages = updatedPost.getImages().stream()
                .map(imageUrl -> new PostImage(imageUrl.getImageUrl(), existingPost))
                .collect(Collectors.toSet());

        /**
         * update content and set new images
         */
        Post uPost = Post.builder()
                .id(existingPost.getId())
                .userId(existingPost.getUserId())
                .likes(existingPost.getLikes())
                .images(newImages)
                .comments(existingPost.getComments())
                //.content(!updatedPost.getContent().equals(existingPost.getContent()) ? updatedPost.getContent() : existingPost.getContent())
                .content(updatedPost.getContent())
                .build();


        return postRepository.save(uPost);
    }

    /**
     * Delete Post by Id
     * @param id
     */
    public void deletePost(long id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            Set<PostImage> postImages = post.getImages();
            postImageRepository.deleteAll(postImages);

            postRepository.delete(post);
        } else {
            throw new IllegalArgumentException("Post with ID " + id + " does not exist.");
        }
    }

    /**
     * @param postId
     * @return no of likes
     */
    public int getLikes(long postId){
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            return post.getLikes().size();
    } else {
        throw new IllegalArgumentException("Could not find post");
    }
    }


}
