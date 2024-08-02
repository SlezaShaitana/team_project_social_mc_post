package com.social.mc_post.dto.mappers;

import com.social.mc_post.dto.CommentDto;
import com.social.mc_post.structure.CommentEntity;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    public CommentDto mapToCommentDto(CommentEntity commentEntity){
        return CommentDto.builder()
                .id(commentEntity.getId())
                .isDeleted(commentEntity.getIsDeleted())
                .time(commentEntity.getTime())
                .timeChanged(commentEntity.getTimeChanged())
                .authorId(commentEntity.getAuthorId())
                .parentId(commentEntity.getParentId())
                .postId(commentEntity.getPostId())
                .commentText(commentEntity.getCommentText())
                .commentType(commentEntity.getCommentType())
                .isBlocked(commentEntity.getIsBlocked())
                .likeAmount(commentEntity.getLikeAmount())
                .myLike(commentEntity.getMyLike())
                .commentsCount(commentEntity.getCommentsCount())
                .imagePath(commentEntity.getImagePath())
                .build();
    }

    public CommentEntity mapToCommentEntity(CommentDto commentDto){
        return CommentEntity.builder()
                .id(commentDto.getId())
                .isDeleted(commentDto.getIsDeleted())
                .time(commentDto.getTime())
                .timeChanged(commentDto.getTimeChanged())
                .authorId(commentDto.getAuthorId())
                .parentId(commentDto.getParentId())
                .postId(commentDto.getPostId())
                .commentText(commentDto.getCommentText())
                .commentType(commentDto.getCommentType())
                .isBlocked(commentDto.getIsBlocked())
                .likeAmount(commentDto.getLikeAmount())
                .myLike(commentDto.getMyLike())
                .commentsCount(commentDto.getCommentsCount())
                .imagePath(commentDto.getImagePath())
                .build();
    }
}
