package com.instagram.Post.service;

import com.instagram.Post.dto.PostCommentDto;
import com.instagram.Post.model.Post;
import com.instagram.Post.model.PostComment;
import com.instagram.Post.repository.PostCommentRepository;
import com.instagram.Post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostCommentService {

    private final PostRepository postRepository;
    private final PostCommentRepository postCommentRepository;

    /**
     * Adds new comment to post
     * @param postCommentDto
     */
    public void addCommentToPost(PostCommentDto postCommentDto){
        Post post = postRepository.findById(postCommentDto.getPostId()).orElseThrow();

        PostComment postComment = PostComment.builder()
                .post(post)
                .content(postCommentDto.getContent())
                .userId(postCommentDto.getUserId())
                .build();
        postCommentRepository.save(postComment);
    }

    /**
     * Update previous comment on post
     * @param id
     * @param postCommentDto
     * @throws ChangeSetPersister.NotFoundException
     */
    public void updateComment(long id,PostCommentDto postCommentDto) throws ChangeSetPersister.NotFoundException {
        PostComment existingComment = postCommentRepository.findById(id)
                .orElseThrow(ChangeSetPersister.NotFoundException::new);
        existingComment.setContent(postCommentDto.getContent());
        postCommentRepository.save(existingComment);
    }

    /**
     * Delete comment by id
     * @param id
     */
    public void deleteComment(long id) {
        Optional<PostComment> optionalPostComment = postCommentRepository.findById(id);
        if (optionalPostComment.isPresent()) {
            PostComment comment = optionalPostComment.get();
            postCommentRepository.delete(comment);
        } else {
            throw new IllegalArgumentException("Post with ID " + id + " does not exist.");
        }
    }
}
