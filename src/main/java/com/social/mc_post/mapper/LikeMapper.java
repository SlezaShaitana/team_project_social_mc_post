package com.social.mc_post.mapper;

import com.social.mc_post.dto.LikeDto;
import com.social.mc_post.model.Like;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
public class LikeMapper {

<<<<<<< HEAD
@Mapper(componentModel = "spring")
public interface LikeMapper {



    LikeEntity mapToLikeEntity(LikeDto likeDto);
    LikeDto mapToLikeDto(LikeEntity likeEntity);
    List<LikeDto> mapToLikeDtoList(List<LikeEntity> likeEntityList);
=======
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
>>>>>>> 37f25c01f13c20b5d3ef933116c30d9e480c03c4
}
