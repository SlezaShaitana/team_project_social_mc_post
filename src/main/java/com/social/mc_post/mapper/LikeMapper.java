package com.social.mc_post.mapper;

import com.social.mc_post.dto.LikeDto;
import com.social.mc_post.model.Like;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
public class LikeMapper {

    public LikeDto mapEntityToDto(Like like){
        String itemId = like.getPost() == null ? like.getComment().getId() : like.getPost().getId();
        return LikeDto.builder()
                .id(like.getId())
                .isDeleted(like.getIsDeleted())
                .authorId(like.getAuthorId())
                .time(like.getTime())
                .itemId(itemId)
                .type(like.getType())
                .reactionType(like.getReaction())
                .build();
    }
}
