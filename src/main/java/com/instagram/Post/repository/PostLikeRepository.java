package com.instagram.Post.repository;

import com.instagram.Post.model.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    boolean existsByPostIdAndUserId(long postId, long userId);
    void deleteByPostIdAndUserId(long postId, long userId);


}
