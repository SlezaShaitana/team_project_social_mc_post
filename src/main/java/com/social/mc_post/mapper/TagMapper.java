package com.social.mc_post.mapper;

import com.social.mc_post.dto.TagDto;
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
}
