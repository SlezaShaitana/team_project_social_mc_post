package com.social.mc_post.services;

import com.social.mc_post.dto.CommentDto;
import com.social.mc_post.dto.CommentSearchDto;
import com.social.mc_post.dto.LikeDto;
import com.social.mc_post.dto.PageableDto;

import java.util.List;

public interface CommentService {

    String createCommentPost(CommentDto commentDto, String postId, String headerRequestByAuth);
    String createSubCommentPost(String idPost, String idComment, CommentDto subComment, String headerRequestByAuth);
    void deleteCommentPost(String postId, String commentId);
    String updateComment(String idComment, CommentDto commentDto);
    String createLikeComment(String idPost, String idComment, LikeDto likeDto, String headerRequestByAuth);
    void removeLikeComment(String idPost, String idComment);

}
