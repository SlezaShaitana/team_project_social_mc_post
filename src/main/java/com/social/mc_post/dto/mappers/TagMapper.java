package com.social.mc_post.dto.mappers;

import com.social.mc_post.dto.TagDto;
import com.social.mc_post.structure.TagEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class TagMapper {

    public TagEntity mapToTagEntity(TagDto tagDto){
        return TagEntity.builder()
                .id(tagDto.getId())
                .isDeleted(tagDto.getIsDeleted())
                .name(tagDto.getName())
                .build();
    }

    public TagDto mapToTagDto(TagEntity tagEntity){
        return TagDto.builder()
                .id(tagEntity.getId())
                .isDeleted(tagEntity.getIsDeleted())
                .name(tagEntity.getName())
                .build();
    }

}
