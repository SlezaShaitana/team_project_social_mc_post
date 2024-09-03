package com.social.mc_post.services;

import com.social.mc_post.dto.CommentDto;
import com.social.mc_post.dto.CommentSearchDto;
import com.social.mc_post.dto.LikeDto;
import com.social.mc_post.dto.PageDto;
import org.springframework.data.domain.Page;

public interface CommentService {

    String createCommentPost(CommentDto commentDto, String postId, String headerRequestByAuth);
    String createSubCommentPost(String idPost, String idComment, CommentDto subComment, String headerRequestByAuth);
    void deleteCommentPost(String postId, String commentId);
    String updateComment(String idComment, CommentDto commentDto);
    String createLikeComment(String idPost, String idComment, LikeDto likeDto, String headerRequestByAuth);
    void removeLikeComment(String idPost, String idComment);
    Page<CommentDto> getCommentsByPostId(CommentSearchDto searchDto, PageDto pageableDto);
    Page<CommentDto> getSubCommentsByPostIdAndCommentId(CommentSearchDto searchDto, PageDto pageableDto);
}
