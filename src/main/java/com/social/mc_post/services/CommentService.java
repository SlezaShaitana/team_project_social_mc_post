package com.social.mc_post.services;

import com.social.mc_post.dto.CommentDto;
import com.social.mc_post.dto.CommentSearchDto;
import com.social.mc_post.dto.PageableDto;
import com.social.mc_post.structure.CommentEntity;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;

public interface CommentService {

    CommentDto createCommentPost(CommentDto commentDto, String postId);
    CommentDto createSubCommentPost(String idPost, String idComment, CommentDto subComment);
    void deleteCommentPost(String postId, String commentId);
    CommentDto updateComment(String idComment, CommentDto commentDto);
    void createLikeComment(String idPost, String idComment, String headerRequestByAuth);
    void removeLikeComment(String idPost, String idComment) throws BadRequestException;
    Page<CommentEntity> getAllComments(String idPost, PageableDto pageableDto,
                                       CommentSearchDto commentSearchDto);
    Page<CommentEntity> getAllSubComments(String idPost, String commentId, PageableDto pageableDto,
                                          CommentSearchDto commentSearchDto);
}
