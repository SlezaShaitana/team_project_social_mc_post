package com.social.mc_post.mapper;

import com.social.mc_post.dto.CommentDto;
import com.social.mc_post.model.Comment;
import com.social.mc_post.repository.CommentRepository;
import com.social.mc_post.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentMapper {

<<<<<<< HEAD
@Mapper(componentModel = "spring")
public interface CommentMapper {


    CommentEntity mapToCommentEntity(CommentDto commentDto);
    CommentDto mapToCommentDto(CommentEntity commentEntity);
    List<CommentEntity> mapToListCommentEntity(List<CommentDto> commentDtoList);
    List<CommentDto> mapToListCommentDto(List<CommentEntity> commentEntityList);
=======
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
>>>>>>> 37f25c01f13c20b5d3ef933116c30d9e480c03c4

    public CommentDto mapEntityToDto(Comment comment){
        String parentId = comment.getParentCommentId() == null ? comment.getPost().getId()
                : comment.getParentCommentId();
        Integer countLikes = likeRepository.countByComment(comment);
        Integer countComments = commentRepository.countByParentCommentId(comment.getId());
        return CommentDto.builder()
                .id(comment.getId())
                .isDeleted(comment.getIsDeleted())
                .commentType(comment.getType())
                .time(comment.getTime())
                .timeChanged(comment.getTimeChanged())
                .authorId(comment.getAuthorId())
                .parentId(parentId)
                .commentText(comment.getCommentText())
                .postId(comment.getPost().getId())
                .isBlocked(comment.getIsBlocked())
                .likeAmount(countLikes)
                .myLike(comment.getMyLike())
                .commentsCount(countComments)
                .imagePath(comment.getImagePath())
                .build();
    }
}
