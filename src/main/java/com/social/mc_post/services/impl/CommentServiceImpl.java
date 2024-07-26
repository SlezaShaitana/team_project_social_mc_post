package com.social.mc_post.services.impl;

import com.social.mc_post.dto.CommentDto;
import com.social.mc_post.dto.mappers.CommentMapper;
import com.social.mc_post.repository.CommentRepository;
import com.social.mc_post.services.CommentService;
import com.social.mc_post.structure.CommentEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public CommentDto createCommentPost(CommentDto commentDto, String postId) {
        CommentEntity newComment = CommentMapper.mapToCommentEntity(commentDto);
        log.info("Text : {}", newComment.getCommentText());
        commentRepository.save(newComment);

        log.info("Comment id : {}", newComment.getId());
        return CommentMapper.mapToCommentDto(newComment);
    }

    @Override
    public void deleteCommentPost(String postId, String commentId) {

    }
}
