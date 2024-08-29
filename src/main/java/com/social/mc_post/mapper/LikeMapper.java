package com.social.mc_post.mapper;

import com.social.mc_post.dto.LikeDto;
import com.social.mc_post.structure.LikeEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LikeMapper {
    LikeEntity mapToLikeEntity(LikeDto likeDto);
    LikeDto mapToLikeDto(LikeEntity likeEntity);
    List<LikeDto> mapToLikeDtoList(List<LikeEntity> likeEntityList);
}
