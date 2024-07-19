package com.social.mc_post.controllers;

import com.social.mc_post.dto.*;
import org.aspectj.bridge.ICommand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class RestPostController {

    @GetMapping("/post")
    public ResponseEntity<?> handlerPost(PostSearchDto searchDto, Pageable pageable){
        return ResponseEntity.ok(searchDto);
    }

    @PutMapping("/post")
    public ResponseEntity<?> putPost(@RequestBody PostDto postDto){
        return ResponseEntity.ok(postDto);
    }

    @PostMapping("/post")
    public ResponseEntity<?> hendlerPost(@RequestBody PostDto postDto){
        return ResponseEntity.ok(postDto);
    }

    @PutMapping("/post/{id}/comment")
    public ResponseEntity<?> hendlerComment(@PathVariable(value = "id") String id){
        return ResponseEntity.ok().body(id);

    }

    @PostMapping("/post/{id}/comment")
    public ResponseEntity<?> hendlerPostComment(@PathVariable(value = "id") String id,
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
                                         CommentSearchDto searchDto, Pageable pageable){
        return null;
    }

    @GetMapping("/post/{postId}/comment/{commentId}/subcomment")
    public ResponseEntity<?> getSubComments(@PathVariable(value = "postId") String postId,
                                         @PathVariable(value = "commentId") String commentId,
                                         CommentSearchDto searchDto, Pageable pageable){
        return null;
    }

    @GetMapping("/post/{id}")
    public ResponseEntity<PostDto> getPost(@PathVariable(value = "id") String id){
        return null;
    }

    @DeleteMapping("/post/{id}")
    public ResponseEntity<PostDto> deletePost(@PathVariable(value = "id") String id){
        return null;
    }


}
