package com.social.mc_post.mapper;

import com.social.mc_post.dto.CommentDto;
import com.social.mc_post.model.Comment;
import com.social.mc_post.repository.CommentRepository;
import com.social.mc_post.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentMapper {

    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;

    public CommentDto mapEntityToDto(Comment comment){
        String parentId = comment.getParentCommentId() == null ? comment.getPost().getId()
                : comment.getParentCommentId();
        Integer countLikes = likeRepository.countByComment(comment);
        Integer countComments = commentRepository.countByParentCommentId(comment.getId());
        return CommentDto.builder()
                .id(comment.getId())
                .isDeleted(comment.getIsDeleted())
                .commentType(comment.getType())
                .time(comment.getTime())
                .timeChanged(comment.getTimeChanged())
                .authorId(comment.getAuthorId())
                .parentId(parentId)
                .commentText(comment.getCommentText())
                .postId(comment.getPost().getId())
                .isBlocked(comment.getIsBlocked())
                .likeAmount(countLikes)
                .myLike(comment.getMyLike())
                .commentsCount(countComments)
                .imagePath(comment.getImagePath())
                .build();
    }
}
