package com.social.mc_post.services;

import com.social.mc_post.dto.CommentDto;

public interface CommentService {

    CommentDto createCommentPost(CommentDto commentDto, String postId);
    void deleteCommentPost(String postId, String commentId);
}
