package com.social.mc_post.dto.mappers;

import com.social.mc_post.dto.PostDto;
import com.social.mc_post.structure.PostEntity;

public class PostMapper {


    /**ToDo Convert PostEntity intro PostDto**/
    public static PostDto mapToPostDto(PostEntity postEntity){
        return PostDto.builder()
                .id(postEntity.getId())
                .isDeleted(postEntity.getIsDeleted())
                .time(postEntity.getTime())
                .timeChanged(postEntity.getTimeChanged())
                .authorId(postEntity.getAuthorId())
                .title(postEntity.getTitle())
                .type(postEntity.getType())
                .postText(postEntity.getPostText())
                .isBlocked(postEntity.getIsBlocked())
                .commentsCount(postEntity.getCommentsCount())
                .tags(postEntity.getTags())
                .reactions(postEntity.getReactions())
                .myReaction(postEntity.getMyReaction())
                .likeAmount(postEntity.getLikeAmount())
                .myLike(postEntity.getMyLike())
                .imagePath(postEntity.getImagePath())
                .publishDate(postEntity.getPublishDate())
                .build();
    }

    /** TODO Convert PostDto intro PostEntity**/
    public static PostEntity mapToPostEntity(PostDto postDto){
        return PostEntity.builder()
                .id(postDto.getId())
                .isDeleted(postDto.getIsDeleted())
                .time(postDto.getTime())
                .timeChanged(postDto.getTimeChanged())
                .authorId(postDto.getAuthorId())
                .title(postDto.getTitle())
                .type(postDto.getType())
                .postText(postDto.getPostText())
                .isBlocked(postDto.getIsBlocked())
                .commentsCount(postDto.getCommentsCount())
                .tags(postDto.getTags())
                .reactions(postDto.getReactions())
                .myReaction(postDto.getMyReaction())
                .likeAmount(postDto.getLikeAmount())
                .myLike(postDto.getMyLike())
                .imagePath(postDto.getImagePath())
                .publishDate(postDto.getPublishDate())
                .build();
    }
}
