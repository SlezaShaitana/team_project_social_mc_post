package com.social.mc_post.services.impl;

import com.social.mc_post.dto.CommentDto;
import com.social.mc_post.dto.CommentSearchDto;
import com.social.mc_post.dto.PageableDto;
import com.social.mc_post.dto.enums.TypeComment;
import com.social.mc_post.dto.enums.TypeLike;
import com.social.mc_post.dto.mappers.CommentMapper;
import com.social.mc_post.exception.PostNotFoundException;
import com.social.mc_post.repository.CommentRepository;
import com.social.mc_post.repository.LikeRepository;
import com.social.mc_post.repository.PostRepository;
import com.social.mc_post.repository.specifications.CommentSpecification;
import com.social.mc_post.services.CommentService;
import com.social.mc_post.structure.CommentEntity;
import com.social.mc_post.structure.LikeEntity;
import com.social.mc_post.structure.PostEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.Date;

@Service
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final CommentSpecification commentSpecification;
    private final CommentMapper commentMapper;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository,
                              PostRepository postRepository,
                              LikeRepository likeRepository,
                              CommentSpecification commentSpecification, CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.likeRepository = likeRepository;
        this.commentSpecification = commentSpecification;
        this.commentMapper = commentMapper;
    }

    @Override
    public CommentDto createCommentPost(CommentDto commentDto, String postId) {
        CommentEntity newComment = commentMapper.mapToCommentEntity(commentDto);
        calculateCountCommentPost(postId, false);

        log.info("Text : {}", newComment.getCommentText());
        commentRepository.save(newComment);

        log.info("Comment id : {}", newComment.getId());
        return commentMapper.mapToCommentDto(newComment);
    }

    @Override
    public CommentDto createSubCommentPost(String idPost, String idComment, CommentDto subComment) {
        PostEntity post = postRepository.findPostEntityById(idPost);
        if (post != null) {
            CommentEntity commentPost = commentRepository.findCommentEntityById(idComment);
            CommentEntity newSubComment = commentMapper.mapToCommentEntity(subComment);
            newSubComment.setParentId(commentPost.getId());
            newSubComment.setPostId(post.getId());
            commentRepository.save(newSubComment);
            log.info("Create subcomment id: {}", newSubComment.getId());
            return commentMapper.mapToCommentDto(newSubComment);
        } else {
            throw new PostNotFoundException("Post not found");
        }
    }

    @Override
    public void deleteCommentPost(String postId, String commentId) {
        calculateCountCommentPost(postId, true);
        CommentEntity comment = commentRepository.findCommentEntityById(commentId);
        commentRepository.delete(comment);
        log.info("Comment id: {} delete by post id: {}", postId, commentId);
    }

    @Override
    public CommentDto updateComment(String idComment, CommentDto commentDto) {
        CommentEntity comment = commentRepository.findCommentEntityById(idComment);
        commentRepository.updateComment(comment, idComment);
        return commentMapper.mapToCommentDto(comment);
    }

    public void calculateCountCommentPost(String idPost, boolean delete) {
        PostEntity post = getPostById(idPost);
        int totalCount = post.getCommentsCount();

        if (delete) {
            post.setCommentsCount(totalCount - 1);
            postRepository.save(post);
        } else {
            post.setCommentsCount(totalCount + 1);
            postRepository.save(post);
        }
    }

    public PostEntity getPostById(String id) {
        return postRepository.findPostEntityById(id);
    }

    public CommentEntity getCommentById(String id) {
        return commentRepository.findCommentEntityById(id);
    }

    @Override
    public void createLikeComment(String idPost, String idComment) throws BadRequestException {
        PostEntity post = getPostById(idPost);
        CommentEntity comment = commentRepository.findCommentEntityById(idComment);
        if (post != null || comment != null) {

            calculateCountCommentPost(post.getId(), false);
            createNewLikeComment(comment.getId());
            calculateCountLikeComment(idComment, false);
        } else {
            throw new BadRequestException("Bad request");
        }
    }

    public void calculateCountLikeComment(String idComment, boolean delete) {
        CommentEntity comment = getCommentById(idComment);
        int totalAmountLike = comment.getLikeAmount();
        if (delete) {
            comment.setLikeAmount(totalAmountLike - 1);
            commentRepository.save(comment);
        } else {
            comment.setLikeAmount(totalAmountLike + 1);
            commentRepository.save(comment);
        }
    }

    public LikeEntity createNewLikeComment(String itemId){
        LikeEntity newLike = LikeEntity.builder()
                .time(new Date())
                .type(TypeLike.COMMENT)
                .itemId(itemId)
                .isDeleted(false)
                .reactionType("")
                .build();
        return likeRepository.save(newLike);
    }

    @Override
    public void removeLikeComment(String idPost, String idComment) throws BadRequestException {
        PostEntity post = getPostById(idPost);
        if (post != null) {
            CommentEntity comment = getCommentById(idComment);
            calculateCountLikeComment(comment.getId(), true);
            commentRepository.delete(comment);
        } else {
            throw new BadRequestException("Bad request");
        }
    }

    @Override
    public Page<CommentEntity> getAllComments(String idPost, PageableDto pageableDto,
                                              CommentSearchDto commentSearchDto){

        Specification<CommentEntity> spec = Specification.where(null);

        if (idPost != null){
            spec.and(commentSpecification.findCommentsByPostId(idPost));
        }

        if (commentSearchDto.getCommentType().equals(TypeComment.COMMENT)){
            spec.and(commentSpecification.findCommentsByCommentType(TypeComment.COMMENT));
        }

        if (commentSearchDto.getAuthorId() != null){
            spec.and(commentSpecification.findCommentsByAuthorId(commentSearchDto.getAuthorId()));
        }

        if (commentSearchDto.getPostId() != null){
            spec.and(commentSpecification.findCommentsByPostId(commentSearchDto.getPostId()));
        }

        return commentRepository.findAll(spec, PageRequest.of(pageableDto.getPage(), pageableDto.getSize()));
    }

    @Override
    public Page<CommentEntity> getAllSubComments(String idPost, String commentId,
                                                 PageableDto pageableDto,
                                                 CommentSearchDto commentSearchDto) {
        Specification<CommentEntity> spec = Specification.where(null);

        if (idPost != null){
            spec.and(commentSpecification.findCommentsByPostId(idPost));
        }

        if (commentSearchDto.getCommentType().equals(TypeComment.COMMENT)){
            spec.and(commentSpecification.findCommentsByCommentType(TypeComment.COMMENT));
        }

        if (commentSearchDto.getAuthorId() != null){
            spec.and(commentSpecification.findCommentsByAuthorId(commentSearchDto.getAuthorId()));
        }

        if (commentSearchDto.getPostId() != null){
            spec.and(commentSpecification.findCommentsByPostId(commentSearchDto.getPostId()));
        }

        if (commentSearchDto.getParentId()!= null){
            spec.and(commentSpecification.findCommentsByParentId(commentSearchDto.getParentId()));
        }

        if (commentId != null){
            spec.and(commentSpecification.findCommentsByParentId(commentId));
        }

        return commentRepository.findAll(spec, PageRequest.of(pageableDto.getPage(), pageableDto.getSize()));

    }
}
