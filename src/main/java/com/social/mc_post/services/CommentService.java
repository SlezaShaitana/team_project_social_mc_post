package com.social.mc_post.services;

import com.social.mc_post.dto.CommentDto;
import com.social.mc_post.dto.CommentSearchDto;
import com.social.mc_post.dto.PageableDto;
import com.social.mc_post.structure.CommentEntity;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CommentService {

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
}
