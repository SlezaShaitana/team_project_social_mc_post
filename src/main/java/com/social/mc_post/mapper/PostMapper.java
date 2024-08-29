package com.social.mc_post.mapper;

import com.social.mc_post.dto.PostDto;
import com.social.mc_post.structure.PostEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMapper {
    PostMapper MAPPER = Mappers.getMapper(PostMapper.class);

    PostEntity mapToPostEntity(PostDto postDto);
    PostDto mapToPostDto(PostEntity postEntity);
    List<PostEntity> mapListToPostEntity(List<PostDto> postDtoList);
    List<PostDto> mapListToPostDto(List<PostEntity> postEntitiesList);

}
