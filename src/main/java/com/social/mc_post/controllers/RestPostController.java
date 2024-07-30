package com.social.mc_post.controllers;

import com.social.mc_post.dto.*;
import com.social.mc_post.exception.PostNotFoundException;
import com.social.mc_post.services.CommentService;
import com.social.mc_post.services.PostService;
import com.social.mc_post.structure.CommentEntity;
import com.social.mc_post.structure.PostEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
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
    private final CommentService commentService;

    @Autowired
    public RestPostController (PostService postService, CommentService commentService){
        this.postService = postService;
        this.commentService = commentService;
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
    public ResponseDto handlerCreatePost(@RequestBody PostDto postDto){
        if (postDto != null){
            log.info("Create new POST: {}", postDto.getTitle());
            postService.createPost(postDto);
            return new ResponseDto(HttpStatus.CREATED.value(), "Successful operation");
        } else {
            try {
                throw new BadRequestException("Bad request");
            } catch (BadRequestException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //TODO Update comment
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

    //TODO  Добавить реализацию добавления сабкоментария
    @PutMapping("/post/{id}/comment/{commentId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto handlerPostSubComment(@PathVariable(value = "id") String id,
                                      @PathVariable(value = "commentId") String commentId,
                                      @RequestBody CommentDto subComment){
        return commentService.createSubCommentPost(id, commentId, subComment);
    }


    //TODO Delete comment post by id
    @DeleteMapping("/post/{id}/comment/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void handlerDeleteComment(@PathVariable(value = "id") String id,
                                                  @PathVariable(value = "commentId") String commentId){
        if (postService.checkPost(id)){
            commentService.deleteCommentPost(id, commentId);
        } else {
            throw new PostNotFoundException("Post not found");
        }
    }

    // TODO Delayed
    @PutMapping("/post/delayed")
    public ResponseEntity handlerDeferredPost(){
        return ResponseEntity.ok().build();
    }

    //TODO Create like post
    @PostMapping("/post/{id}/like")
    @ResponseStatus(HttpStatus.CREATED)
    public LikeDto handlerLike(@PathVariable(value = "id") String id,
                                         @RequestBody LikeDto likeDto){
        return postService.createLikePost(id, likeDto);
    }

    // TODO Удаление лайка типа пост
    @DeleteMapping("/post/{id}/like")
    public ResponseEntity<?> handlerDeleteLike(@PathVariable(value = "id") String id){
        return ResponseEntity.ok("Hello");
    }

    // TODO create like comment
    @PostMapping("/post/{id}/comment/{commentId}/like")
    public ResponseDto handlerPostLikeComment(@PathVariable(value = "id") String idPost,
                                                    @PathVariable(value = "commentId") String commentId) throws BadRequestException {
        commentService.createLikeComment(idPost, commentId);
        return new ResponseDto(HttpStatus.CREATED.value(), "Succfal");
    }

    // TODO remove like comment
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

    //TODO Get subcomment
    @GetMapping("/post/{postId}/comment/{commentId}/subcomment")
    public Page<CommentEntity> getSubComments(@PathVariable(value = "postId") String postId,
                                         @PathVariable(value = "commentId") String commentId,
                                         CommentSearchDto searchDto, PageableDto page){
        log.info("GEt sub comments: {}", commentId);
        return commentService.getAllSubComments(postId, commentId, page, searchDto);
    }

    //TODO Get post by id
    @GetMapping("/post/{id}")
    public PostDto getPost(@PathVariable(value = "id") String id){
        return postService.getPostById(id);
    }

    //TODO Delete post by id
    @DeleteMapping("/post/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable(value = "id") String id){
        postService.deletePost(id);
    }
}
