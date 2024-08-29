package com.social.mc_post.mapper;

import com.social.mc_post.dto.TagDto;
import com.social.mc_post.structure.TagEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TagMapper {
    TagDto mapToTagDto(TagEntity tagEntity);
    TagEntity mapToTagEntity(TagDto tagDto);
}
