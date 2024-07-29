package com.social.mc_post.dto.mappers;

import com.social.mc_post.dto.LikeDto;
import com.social.mc_post.structure.LikeEntity;

public class LikeMapper {

    public static LikeDto mapToLikeDto(LikeEntity likeEntity){
        return LikeDto.builder()
                .id(likeEntity.getId())
                .isDeleted(likeEntity.getIsDeleted())
                .authorId(likeEntity.getAuthorId())
                .time(likeEntity.getTime())
                .itemId(likeEntity.getItemId())
                .type(likeEntity.getType())
                .reactionType(likeEntity.getReactionType())
                .build();
    }

    public static LikeEntity mapToLikeEntity(LikeDto likeDto){
        return LikeEntity.builder()
                .id(likeDto.getId())
                .isDeleted(likeDto.getIsDeleted())
                .authorId(likeDto.getAuthorId())
                .time(likeDto.getTime())
                .itemId(likeDto.getItemId())
                .type(likeDto.getType())
                .reactionType(likeDto.getReactionType())
                .build();
    }
}
