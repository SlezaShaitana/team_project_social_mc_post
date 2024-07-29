package com.social.mc_post.services.impl;

import com.social.mc_post.dto.CommentDto;
import com.social.mc_post.dto.mappers.CommentMapper;
import com.social.mc_post.exception.PostNotFoundException;
import com.social.mc_post.repository.CommentRepository;
import com.social.mc_post.repository.PostRepository;
import com.social.mc_post.services.CommentService;
import com.social.mc_post.structure.CommentEntity;
import com.social.mc_post.structure.PostEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CommentServiceImpl implements CommentService {
    private final PostRepository postRepository;

    private final CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository,
                              PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
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
    public CommentDto createSubCommentPost(String idPost, String idComment, CommentDto subComment) {
        PostEntity post = postRepository.findPostEntityById(idPost);
        if (post != null) {
            CommentEntity commentPost = commentRepository.findCommentEntityById(idComment);
            CommentEntity newSubComment = CommentMapper.mapToCommentEntity(subComment);
            newSubComment.setParentId(commentPost.getId());
            newSubComment.setPostId(post.getId());
            commentRepository.save(newSubComment);
            log.info("Create subcomment id: {}", newSubComment.getId());
            return CommentMapper.mapToCommentDto(newSubComment);
        } else {
            throw new PostNotFoundException("Post not found");
        }
    }

    @Override
    public void deleteCommentPost(String postId, String commentId) {

    }
}
