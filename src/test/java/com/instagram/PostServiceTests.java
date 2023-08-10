package com.instagram;


import com.instagram.Post.controller.PostController;
import com.instagram.Post.model.Post;
import com.instagram.Post.model.PostImage;
import com.instagram.Post.model.PostLike;
import com.instagram.Post.repository.PostImageRepository;
import com.instagram.Post.repository.PostRepository;
import com.instagram.Post.service.PostService;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.crossstore.ChangeSetPersister;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PostServiceTests {

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostImageRepository postImageRepository;

    @InjectMocks
    private PostService postService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testUpdatePost_Success() throws ChangeSetPersister.NotFoundException {
        // Given
        long postId = 1L;
        Post existingPost  = Mockito.mock(Post.class);
        Post updatedPost = Mockito.mock(Post.class);

        when(postRepository.findById(postId)).thenReturn(Optional.of(existingPost));
        doNothing().when(postImageRepository).delete(any(PostImage.class));
        when(postRepository.save(any(Post.class))).thenReturn(updatedPost);

        // When
        Post result = postService.updatePost(postId, updatedPost);

        // Then
        assertEquals(updatedPost, result);

    }


    @Test
    public void testUpdatePost_PostNotFound() {
        // Given
        long postId = 1L;
        Post updatedPost = Mockito.mock(Post.class);
        updatedPost.setId(postId);
        updatedPost.setContent("Updated content");

        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        // When and Then
        assertThrows(ChangeSetPersister.NotFoundException.class, () -> postService.updatePost(postId, updatedPost));
        verify(postRepository).findById(postId);
        verifyNoMoreInteractions(postRepository, postImageRepository);

    }

    @Test
    public void testDeletePost_Success() {
        // Given
        long postId = 1L;
        Post post = Mockito.mock(Post.class);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        // When
        postService.deletePost(postId);

        // Then
        verify(postRepository).delete(post);

    }
    @Test
    public void testDeletePost_PostNotFound() {
        // Given
        long postId = 1L;

        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        // When and Then
        assertThrows(IllegalArgumentException.class, () -> postService.deletePost(postId));
        verify(postRepository).findById(postId);
        verifyNoMoreInteractions(postRepository, postImageRepository);
    }

    @Test
    public void testGetLikes_Success() {
        // Given
        long postId = 1L;
        Post post = Mockito.mock(Post.class);
        PostLike like1 = Mockito.mock(PostLike.class);
        Set<PostLike> postLikes = new HashSet<>();
        postLikes.add(like1);

        Mockito.when(post.getId()).thenReturn(postId);
        Mockito.when(post.getLikes()).thenReturn(postLikes);
        Mockito.when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        // When
        int likesCount = postService.getLikes(postId);

        // Then
        assertEquals(post.getLikes().size(), likesCount);
        verify(postRepository).findById(postId);
    }

    @Test
    void testGetLikes_PostNotFound() {
        // Given
        long postId = 1L;

        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        // When and Then
        assertThrows(IllegalArgumentException.class, () -> postService.getLikes(postId));
        verify(postRepository).findById(postId);
        verifyNoMoreInteractions(postRepository);
    }
}