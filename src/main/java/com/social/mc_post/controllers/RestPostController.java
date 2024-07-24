package com.social.mc_post.controllers;

import com.social.mc_post.dto.*;
import com.social.mc_post.services.PostService;
import com.social.mc_post.structure.PostEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class RestPostController {

    private final PostService postService;

    @Autowired
    public RestPostController (PostService postService){
        this.postService = postService;
    }

    @GetMapping("/post")
    public Page<PostEntity> handlerPost(PostSearchDto searchDto, PageableDto pageableDto){
        return postService.getPosts(searchDto, pageableDto);
    }

    @PutMapping("/post")
    public PostDto handlerUpdatePost(@RequestBody PostDto postDto){
        return postService.updatePost(postDto);
    }

    @PostMapping("/post")
    @ResponseStatus(HttpStatus.CREATED)
    public PostDto handlerCreatePost(@RequestBody PostDto postDto){
        log.info(postDto.toString());
        return postService.createPost(postDto);
    }

    @PutMapping("/post/{id}/comment")
    public ResponseEntity<?> handlerComment(@PathVariable(value = "id") String id){
        return ResponseEntity.ok().body(id);

    }

    @PostMapping("/post/{id}/comment")
    public ResponseEntity<?> handlerPostComment(@PathVariable(value = "id") String id,
                                                @RequestBody CommentDto commentDto){
        return ResponseEntity.ok(commentDto);
    }

    @PutMapping("/post/{id}/comment/{commentId}")
    public ResponseEntity<?> handlerPostSubComment(@PathVariable(value = "id") String id,
                                                   @PathVariable(value = "commentId") String commentId,
                                                   @RequestBody CommentDto commentDto){
        return ResponseEntity.ok(commentDto);
    }

    @DeleteMapping("/post/{id}/comment/{commentId}")
    public ResponseEntity<?> handlerDeleteComment(@PathVariable(value = "id") String id,
                                                  @PathVariable(value = "commentId") String commentId){
        return ResponseEntity.ok().body("Hello");
    }

    @PutMapping("/post/delayed")
    public ResponseEntity deferredPostHandler(){
        return ResponseEntity.ok().build();
    }

    @PostMapping("/post/{id}/like")
    public ResponseEntity<?> handlerLike(@PathVariable(value = "id") String id,
                                         @RequestBody LikeDto likeDto){
        return ResponseEntity.ok(likeDto);
    }

    @DeleteMapping("/post/{id}/like")
    public ResponseEntity<?> handlerDeleteLike(@PathVariable(value = "id") String id){
        return ResponseEntity.ok("Hello");
    }

    @PostMapping("/post/{id}/comment/{commentId}/like")
    public ResponseEntity<?> handlerPostLikeComment(@PathVariable(value = "id") String id,
                                                    @PathVariable(value = "commentId") String commentId){
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/post/{id}/comment/{commentId}/like")
    public ResponseEntity<?> handlerDeleteLikeComment(@PathVariable(value = "id") String id,
                                                    @PathVariable(value = "commentId") String commentId){
        return ResponseEntity.ok().build();
    }

    @GetMapping("/post/{postId}/comment")
    public ResponseEntity<?> getComments(@PathVariable(value = "postId") String postId,
                                         CommentSearchDto searchDto, PageableDto pageableDto){
        return null;
    }

    @GetMapping("/post/{postId}/comment/{commentId}/subcomment")
    public ResponseEntity<?> getSubComments(@PathVariable(value = "postId") String postId,
                                         @PathVariable(value = "commentId") String commentId,
                                         CommentSearchDto searchDto, PageableDto pageableDto){
        return null;
    }

    @GetMapping("/post/{id}")
    public ResponseEntity<PostDto> getPost(@PathVariable(value = "id") String id){
        return null;
    }

    @DeleteMapping("/post/{id}")

    public void deletePost(@PathVariable(value = "id") String id){
        postService.deletePost(id);
    }


}
