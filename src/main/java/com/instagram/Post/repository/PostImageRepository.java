package com.instagram.Post.repository;

import com.instagram.Post.model.Post;
import com.instagram.Post.model.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostImageRepository extends JpaRepository<PostImage,Long> {

    @Modifying
    @Query("DELETE FROM PostImage p WHERE p.post = :post")
    void deleteByPost(@Param("post") Post post);



}
