package com.social.mc_post.mapper;

import com.social.mc_post.dto.CommentDto;
import com.social.mc_post.structure.CommentEntity;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    CommentEntity mapToCommentEntity(CommentDto commentDto);
    CommentDto mapToCommentDto(CommentEntity commentEntity);
    List<CommentEntity> mapToListCommentEntity(List<CommentDto> commentDtoList);
    List<CommentDto> mapToListCommentDto(List<CommentEntity> commentEntityList);

}
