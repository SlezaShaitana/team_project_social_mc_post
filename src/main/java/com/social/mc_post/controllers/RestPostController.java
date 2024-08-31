package com.social.mc_post.controllers;

import com.social.mc_post.dto.*;
import com.social.mc_post.exception.ResourceNotFoundException;
import com.social.mc_post.services.CommentService;
import com.social.mc_post.services.PostService;
import com.social.mc_post.structure.CommentEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class RestPostController {

    private final PostService postService;
    private final CommentService commentService;


    @GetMapping("/post")
    public Page<PostDto> handlerPost(PostSearchDto searchDto, PageableDto pageableDto){
        return postService.getPosts(searchDto, pageableDto);
    }

    @PutMapping("/post")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void handlerUpdatePost(@RequestBody PostDto postDto){
        postService.updatePost(postDto);
    }

    @PostMapping("/post")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto handlerCreatePost(@RequestBody PostDto postDto,
                                         @RequestHeader("Authorization") String token){
        log.info(postDto.toString());
        log.info("Create new POST: {}", postDto.getTitle());
        postService.createPost(postDto, token);
        return new ResponseDto(HttpStatus.CREATED.value(), "Successful operation");
    }

    @PutMapping("/post/{id}/comment")
    public CommentDto handlerComment(@PathVariable(value = "id") String id,
                                            @RequestBody CommentDto commentDto){
        return commentService.updateComment(id, commentDto);
    }

    @PostMapping("/post/{id}/comment")
    public CommentDto handlerPostComment(@PathVariable(value = "id") String id,
                                                @RequestBody CommentDto commentDto){
        return commentService.createCommentPost(commentDto, id);
    }

    @PutMapping("/post/{id}/comment/{commentId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto handlerPostSubComment(@PathVariable(value = "id") String id,
                                      @PathVariable(value = "commentId") String commentId,
                                      @RequestBody CommentDto subComment){
        return commentService.createSubCommentPost(id, commentId, subComment);
    }

    @DeleteMapping("/post/{id}/comment/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void handlerDeleteComment(@PathVariable(value = "id") String id,
                                                  @PathVariable(value = "commentId") String commentId){
        if (postService.checkPost(id)){
            commentService.deleteCommentPost(id, commentId);
        } else {
            throw new ResourceNotFoundException("Пост не найден");
        }
    }

    @PutMapping("/post/delayed")
    public String handlerDeferredPost(@RequestHeader("Authorization") String token){
        return postService.delayed(token);
    }

    @PostMapping("/post/{id}/like")
    @ResponseStatus(HttpStatus.CREATED)
    public LikeDto handlerLike(@PathVariable(value = "id") String id,
                                         @RequestBody LikeDto likeDto){
        return postService.createLikePost(id, likeDto);
    }

    @DeleteMapping("/post/{id}/like")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void handlerDeleteLike(@PathVariable(value = "id") String id) throws BadRequestException {
        postService.deleteLike(id);
    }

    @PostMapping("/post/{id}/comment/{commentId}/like")
    public ResponseDto handlerPostLikeComment(@PathVariable(value = "id") String idPost,
                                              @PathVariable(value = "commentId") String commentId,
                                              @RequestHeader("Authorization") String headerRequestByAuth) throws BadRequestException {
        commentService.createLikeComment(idPost, commentId, headerRequestByAuth);
        return new ResponseDto(HttpStatus.CREATED.value(), "Successful operation");
    }

    @DeleteMapping("/post/{id}/comment/{commentId}/like")
    public ResponseDto handlerDeleteLikeComment(@PathVariable(value = "id") String idPost,
                                                    @PathVariable(value = "commentId") String commentId) throws BadRequestException {
        commentService.removeLikeComment(idPost, commentId);
        return new ResponseDto(HttpStatus.OK.value(), "Successful operation");
    }

    @GetMapping("/post/{postId}/comment")
    public Page<CommentEntity> getComments(@PathVariable(value = "postId") String postId,
                                           CommentSearchDto searchDto, PageableDto page){

        if (page.getPage() < 1){
            page.setPage(1);
        }

        return commentService.getAllComments(postId, page, searchDto);
    }

    @GetMapping("/post/{postId}/comment/{commentId}/subcomment")
    public Page<CommentEntity> getSubComments(@PathVariable(value = "postId") String postId,
                                         @PathVariable(value = "commentId") String commentId,
                                         CommentSearchDto searchDto, PageableDto page){
        log.info("GEt sub comments: {}", commentId);
        return commentService.getAllSubComments(postId, commentId, page, searchDto);
    }

    @GetMapping("/post/{id}")
    public PostDto getPost(@PathVariable(value = "id") String id){
        return postService.getPostById(id);
    }

    @DeleteMapping("/post/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable(value = "id") String id){
        log.info("Delete post by id: {}", id);
        postService.deletePost(id);
    }
}
