package com.instagram.Post.service;

import com.instagram.Post.model.Post;
import com.instagram.Post.model.PostLike;
import com.instagram.Post.repository.PostLikeRepository;
import com.instagram.Post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private  final PostRepository postRepository;

    /**
     * Method to like a post, it saves the userid and the postid that is liked.
     * @param uid
     * @param likedPostId
     */
    @Transactional
    public void likePost(long uid, long likedPostId) {
        Post post = postRepository.findById(likedPostId).orElseThrow();

        /**
         * if post is not already liked, add new like, if it is already liked remove it back
         */
        boolean alreadyLiked = postLikeRepository.existsByPostIdAndUserId(likedPostId,uid);
       if(!alreadyLiked){
           PostLike postLike = PostLike.builder()
                   .post(post)
                   .userId(uid)
                   .build();
           postLikeRepository.save(postLike);
       }
       else{
           postLikeRepository.deleteByPostIdAndUserId(likedPostId,uid);
       }
    }
}
