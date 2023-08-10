package com.instagram.Post.controller;

import com.instagram.Post.service.PostCommentService;
import com.instagram.Post.service.PostLikeService;
import com.instagram.Post.service.PostService;
import com.instagram.Post.dto.AddPostDto;
import com.instagram.Post.dto.PostCommentDto;
import com.instagram.Post.model.Post;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;
    private final PostCommentService postCommentService;
    private final PostLikeService postLikService;

    public PostController(PostService postService, PostCommentService postCommentService, PostLikeService postLikService) {
        this.postService = postService;
        this.postCommentService = postCommentService;
        this.postLikService = postLikService;
    }
    @GetMapping("/")
    public List<Post> getPosts(){
        return postService.getAllPosts();

    }
    @PostMapping("/")
    public void addPost(@RequestBody AddPostDto postdto){
        postService.createPost(postdto);
    }
    @PutMapping("/{id}")
    public Post updatePost(@PathVariable long id, @RequestBody Post post) throws ChangeSetPersister.NotFoundException {
        return postService.updatePost(id,post);
    }
    @GetMapping("/{id}")
    public Optional<Post> getPostById(@PathVariable long id) {
        return postService.getPostById(id);
    }
    @GetMapping("user/{userid}")
    public List<Post> getPostsByUserId(@PathVariable long userid)
    {
        return postService.getPostsByUserId(userid);
    }

    @PostMapping("/addComment")
    public String addComment(@RequestBody PostCommentDto commentDto){
        postService.addComment(commentDto);
        return "Successful";
    }
    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable long id){
        postService.deletePost(id);
    }
    @PutMapping("/comment/{id}")
    public void updateComment(@PathVariable long id,@RequestBody PostCommentDto postCommentDto) throws ChangeSetPersister.NotFoundException {
        postCommentService.updateComment(id, postCommentDto);
    }
    @DeleteMapping("/comment/{id}")
    public void deleteComment(@PathVariable long id){
        postCommentService.deleteComment(id);
    }

    @PostMapping("/likepost")
    void likePost(@RequestParam("uid") long uid, @RequestParam("postId") long likedPostId){
        postLikService.likePost(uid,likedPostId);
    }
    @GetMapping("getLikes/{id}")
    public long getLikes(@PathVariable long id) {
        return postService.getLikes(id);
    }

}
