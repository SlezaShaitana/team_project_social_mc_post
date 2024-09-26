package com.social.mc_post.services;

import com.social.mc_post.dto.CommentDto;
import com.social.mc_post.dto.CommentSearchDto;
import com.social.mc_post.dto.LikeDto;
import com.social.mc_post.dto.PageDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CommentService {

<<<<<<< HEAD
    void createCommentPost(CommentDto commentDto, String postId, String token);
    void createSubCommentPost(String idPost, String idComment, CommentDto subComment, String token);
    void deleteCommentPost(String postId, String commentId);
    void updateComment(String idComment, CommentDto commentDto);
    void createLikeComment(String idPost, String idComment, String headerRequestByAuth);
    void removeLikeComment(String idPost, String idComment) throws BadRequestException;
    Page<CommentEntity> getAllComments(String idPost, PageableDto pageableDto,
                                       CommentSearchDto commentSearchDto);
    Page<CommentEntity> getAllSubComments(String idPost, String commentId, PageableDto pageableDto,
                                          CommentSearchDto commentSearchDto);
    Page<CommentEntity> getCommentsPost(String postId, PageableDto page, CommentSearchDto searchDto);
=======
    String createCommentPost(CommentDto commentDto, String postId, String headerRequestByAuth);
    String createSubCommentPost(String idPost, String idComment, CommentDto subComment, String headerRequestByAuth);
    void deleteCommentPost(String postId, String commentId, String headerRequestByAuth);
    String updateComment(String idComment, CommentDto commentDto, String headerRequestByAuth);
    LikeDto createLikeComment(String idPost, String idComment, String headerRequestByAuth);
    void removeLikeComment(String idPost, String idComment, String headerRequestByAuth);
    Page<CommentDto> getCommentsByPostId(CommentSearchDto searchDto, PageDto pageableDto);
    Page<CommentDto> getSubCommentsByPostIdAndCommentId(CommentSearchDto searchDto, PageDto pageableDto);
>>>>>>> 37f25c01f13c20b5d3ef933116c30d9e480c03c4
}
