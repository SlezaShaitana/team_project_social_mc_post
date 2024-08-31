package com.social.mc_post.mapper;

import com.social.mc_post.dto.TagDto;
import com.social.mc_post.structure.TagEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TagMapper {

    TagMapper MAPPER = Mappers.getMapper(TagMapper.class);

    TagDto mapToTagDto(TagEntity tagEntity);
    TagEntity mapToTagEntity(TagDto tagDto);
    List<TagEntity> mapToListTagEntity(List<TagDto> tagDtos);
    List<TagDto> mapToListTagDto(List<TagEntity> tagEntities);
}
