package com.social.mc_post.mapper;

import com.social.mc_post.dto.TagDto;
<<<<<<< HEAD
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
=======
import com.social.mc_post.model.Post;
import com.social.mc_post.model.Tag;
import org.springframework.stereotype.Component;

@Component
public class TagMapper {

    public Tag mapDtoToEntity(TagDto tagDto, Post post){
        return new Tag(null, false, tagDto.getName(), post);
    }

    public TagDto mapEntityToDto(Tag tag){
        return new TagDto(tag.getId(), tag.getIsDeleted(),tag.getName());
    }
>>>>>>> 37f25c01f13c20b5d3ef933116c30d9e480c03c4
}
