package com.social.mc_post.controllers;

import com.social.mc_post.dto.*;
import com.social.mc_post.exception.ResourceNotFoundException;
import com.social.mc_post.security.DecodedToken;
import com.social.mc_post.services.CommentService;
import com.social.mc_post.services.PostService;
import com.social.mc_post.utils.UrlParseUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;
    private final CommentService commentService;


    @GetMapping("/post")
    public Page<PostDto> getListPosts(PostSearchDto searchDto,
                                      PageDto pageableDto,
                                      @RequestHeader("Authorization") String headerRequestByAuth){
            return postService.getPosts(searchDto, pageableDto, headerRequestByAuth);
    }

    @PutMapping("/post")
    public ResponseDto handlerUpdatePost(@RequestBody PostDto postDto,
                                     @RequestHeader("Authorization") String headerRequestByAuth){
        return new ResponseDto(HttpStatus.OK.value(),
                postService.updatePost(postDto, headerRequestByAuth));
    }

    @PostMapping("/post")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto handlerCreatePost(@RequestBody PostDto postDto,
                                         @RequestHeader("Authorization") String headerRequestByAuth){
        return new ResponseDto(HttpStatus.CREATED.value(),
                postService.createPost(postDto, headerRequestByAuth));
    }

    @PutMapping("/post/{id}/comment")
    public ResponseDto handlerComment(@PathVariable(value = "id") String id,
                                     @RequestBody CommentDto commentDto,
                                      @RequestHeader("Authorization") String headerRequestByAuth){
        return new ResponseDto(HttpStatus.CREATED.value()
                ,commentService.updateComment(id, commentDto, headerRequestByAuth));
    }

    @PostMapping("/post/{id}/comment")
    public ResponseDto handlerPostComment(@PathVariable(value = "id") String id,
                                         @RequestBody CommentDto commentDto,
                                         @RequestHeader("Authorization") String headerRequestByAuth){
        return new ResponseDto(HttpStatus.OK.value(),
                commentService.createCommentPost(commentDto, id, headerRequestByAuth));
    }

    @PutMapping("/post/{id}/comment/{commentId}")
    public ResponseDto handlerPostSubComment(@PathVariable String id,
                                        @PathVariable(value = "commentId") String commentId,
                                        @RequestBody CommentDto subComment,
                                        @RequestHeader("Authorization") String headerRequestByAuth){
        return new ResponseDto(HttpStatus.OK.value(),
                commentService.createSubCommentPost(id, commentId, subComment, headerRequestByAuth));
    }

    @DeleteMapping("/post/{id}/comment/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void handlerDeleteComment(@PathVariable(value = "id") String id,
                                     @PathVariable(value = "commentId") String commentId,
                                     @RequestHeader("Authorization") String headerRequestByAuth){
        commentService.deleteCommentPost(id, commentId, headerRequestByAuth);
    }

    @PutMapping("/post/delayed")
    public String handlerDeferredPost(@RequestHeader("Authorization") String token){
        return postService.delayed(token);
    }

    @PostMapping("/post/{id}/like")
    @ResponseStatus(HttpStatus.CREATED)
    public LikeDto handlerLike(@PathVariable(value = "id") String id,
                               @RequestBody LikeDto likeDto,
                               @RequestHeader("Authorization") String headerRequestByAuth){
        return postService.createLikePost(id, likeDto, headerRequestByAuth);
    }

    @DeleteMapping("/post/{id}/like")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void handlerDeleteLike(@PathVariable(value = "id") String id,
                                  @RequestHeader("Authorization") String headerRequestByAuth){
        postService.deleteLike(id, headerRequestByAuth);
    }

    @PostMapping("/post/{id}/comment/{commentId}/like")
    @ResponseStatus(HttpStatus.CREATED)
    public LikeDto handlerPostLikeComment(@PathVariable String id,
                                              @PathVariable String commentId,
                                              @RequestHeader("Authorization") String headerRequestByAuth){
        return commentService.createLikeComment(id, commentId, headerRequestByAuth);
    }

    @DeleteMapping("/post/{id}/comment/{commentId}/like")
    public ResponseDto handlerDeleteLikeComment(@PathVariable(value = "id") String idPost,
                                                    @PathVariable(value = "commentId") String commentId,
                                                @RequestHeader("Authorization") String headerRequestByAuth){
        commentService.removeLikeComment(idPost, commentId, headerRequestByAuth);
        return new ResponseDto(HttpStatus.OK.value(), "Successful operation");
    }

    @GetMapping("/post/{postId}/comment")
    public Page<CommentDto> getComments(@PathVariable(value = "postId") String postId,
                                        CommentSearchDto searchDto, PageDto pageableDto){
        try {
            if (searchDto == null){
                searchDto = new CommentSearchDto();
            }
            searchDto.setPostId(postId);
            return commentService.getCommentsByPostId(searchDto, pageableDto);
        }catch (Exception e){
            throw new ResourceNotFoundException("Error: " + e.getMessage());
        }
    }

    @GetMapping("/post/{postId}/comment/{commentId}/subcomment")
    public Page<CommentDto> getSubComments(@PathVariable(value = "postId") String postId,
                                         @PathVariable(value = "commentId") String commentId,
                                           CommentSearchDto searchDto, PageDto pageableDto){
        try {
            if (searchDto == null){
                searchDto = new CommentSearchDto();
            }
            searchDto.setPostId(postId);
            searchDto.setParentId(commentId);
            return commentService.getSubCommentsByPostIdAndCommentId(searchDto, pageableDto);
        }catch (Exception e){
            throw new ResourceNotFoundException("Error: " + e.getMessage());
        }
    }

    @GetMapping("/post/{id}")
    public PostDto getPost(@PathVariable(value = "id") String id){
        return postService.getPostDtoById(id);
    }

    @DeleteMapping("/post/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable String id,
                           @RequestHeader("Authorization") String headerRequestByAuth){
        postService.deletePost(id, headerRequestByAuth);
    }
}
