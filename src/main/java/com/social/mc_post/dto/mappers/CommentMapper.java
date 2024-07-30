package com.social.mc_post.dto.mappers;

import com.social.mc_post.dto.CommentDto;
import com.social.mc_post.dto.PostDto;
import com.social.mc_post.structure.CommentEntity;
import com.social.mc_post.structure.PostEntity;

import java.util.ArrayList;
import java.util.List;

public class CommentMapper {

    public static CommentDto mapToCommentDto(CommentEntity commentEntity){
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

    public static CommentEntity mapToCommentEntity(CommentDto commentDto){
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

    public static List<CommentEntity> mapToCommentEntityList(List<CommentDto> commentDto){
        List<CommentEntity> commentEntities = new ArrayList<>();
        for (CommentDto comment : commentDto){
            CommentEntity newComment = CommentEntity.builder()
                    .id(comment.getId())
                    .isDeleted(comment.getIsDeleted())
                    .time(comment.getTime())
                    .timeChanged(comment.getTimeChanged())
                    .authorId(comment.getAuthorId())
                    .parentId(comment.getParentId())
                    .postId(comment.getPostId())
                    .commentText(comment.getCommentText())
                    .commentType(comment.getCommentType())
                    .isBlocked(comment.getIsBlocked())
                    .likeAmount(comment.getLikeAmount())
                    .myLike(comment.getMyLike())
                    .commentsCount(comment.getCommentsCount())
                    .imagePath(comment.getImagePath())
                    .build();
            commentEntities.add(newComment);
        }
       return commentEntities;
    }

    public static List<CommentDto> mapToCommentDtoList(List<CommentEntity> commentEntities){
        List<CommentDto> commentDto = new ArrayList<>();
        for (CommentEntity comment : commentEntities){
            CommentDto newComment = CommentDto.builder()
                    .id(comment.getId())
                    .isDeleted(comment.getIsDeleted())
                    .time(comment.getTime())
                    .timeChanged(comment.getTimeChanged())
                    .authorId(comment.getAuthorId())
                    .parentId(comment.getParentId())
                    .postId(comment.getPostId())
                    .commentText(comment.getCommentText())
                    .commentType(comment.getCommentType())
                    .isBlocked(comment.getIsBlocked())
                    .likeAmount(comment.getLikeAmount())
                    .myLike(comment.getMyLike())
                    .commentsCount(comment.getCommentsCount())
                    .imagePath(comment.getImagePath())
                    .build();
            commentDto.add(newComment);
        }
        return commentDto;
    }

}
