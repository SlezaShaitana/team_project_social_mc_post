package com.social.mc_post.services.impl;

import com.social.mc_post.dto.CommentDto;
import com.social.mc_post.dto.CommentSearchDto;
import com.social.mc_post.dto.PageableDto;
import com.social.mc_post.dto.enums.TypeComment;
import com.social.mc_post.dto.enums.TypeLike;
import com.social.mc_post.mapper.CommentMapper;
import com.social.mc_post.dto.notification.MicroServiceName;
import com.social.mc_post.dto.notification.NotificationDTO;
import com.social.mc_post.dto.notification.NotificationType;
import com.social.mc_post.exception.ResourceNotFoundException;
import com.social.mc_post.kafka.KafkaProducer;
import com.social.mc_post.repository.CommentRepository;
import com.social.mc_post.repository.LikeRepository;
import com.social.mc_post.repository.PostRepository;
import com.social.mc_post.repository.specifications.CommentSpecification;
import com.social.mc_post.security.DecodedToken;
import com.social.mc_post.services.CommentService;
import com.social.mc_post.structure.CommentEntity;
import com.social.mc_post.structure.LikeEntity;
import com.social.mc_post.structure.PostEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final CommentSpecification commentSpecification;
    private final CommentMapper commentMapper;
    private final KafkaProducer producer;


    @Override
    public CommentDto createCommentPost(CommentDto commentDto, String postId) {
        CommentEntity newComment = commentMapper.mapToCommentEntity(commentDto);
        calculateCountCommentPost(postId, false);
        commentRepository.save(newComment);
        log.info("Comment id : {}", newComment.getId());
        PostEntity post = postRepository.findPostEntityById(postId);
        producer.sendMessageForNotification(NotificationDTO.builder()
                        .id(UUID.randomUUID())
                        .authorId(UUID.fromString(newComment.getAuthorId()))
                        .content(newComment.getCommentText())
                        .notificationType(NotificationType.POST_COMMENT)
                        .sentTime(LocalDateTime.now())
                        .receiverId(UUID.fromString(post.getAuthorId()))
                        .serviceName(MicroServiceName.POST)
                .build());
        return commentMapper.mapToCommentDto(newComment);
    }

    @Override
    public CommentDto createSubCommentPost(String idPost, String idComment, CommentDto subComment) {
        Optional<PostEntity> post = postRepository.findById(idPost);

        if (post.isPresent()) {

            CommentEntity commentPost = commentRepository.findCommentEntityById(idComment);
            CommentEntity newSubComment = commentMapper.mapToCommentEntity(subComment);
            newSubComment.setParentId(commentPost.getId());
            newSubComment.setPost(post.get());
            commentRepository.save(newSubComment);
            log.info("Create subcomment id: {}", newSubComment.getId());

            producer.sendMessageForNotification(NotificationDTO.builder()
                    .id(UUID.randomUUID())
                    .authorId(UUID.fromString(subComment.getAuthorId()))
                    .content(subComment.getCommentText())
                    .notificationType(NotificationType.COMMENT_COMMENT)
                    .sentTime(LocalDateTime.now())
                    .receiverId(UUID.fromString(commentPost.getAuthorId()))
                    .serviceName(MicroServiceName.POST)
                    .build());

            return commentMapper.mapToCommentDto(newSubComment);
        } else {
            throw new ResourceNotFoundException("Пост не найден");
        }
    }

    @Override
    public void deleteCommentPost(String postId, String commentId) {
        calculateCountCommentPost(postId, true);
        CommentEntity comment = commentRepository.findCommentEntityById(commentId);
        commentRepository.delete(comment);
        log.info("Comment id: {} delete by post id: {}", postId, commentId);
    }

    @Override
    public CommentDto updateComment(String idComment, CommentDto commentDto) {
        CommentEntity comment = commentRepository.findCommentEntityById(idComment);
        commentRepository.updateComment(comment, idComment);
        return commentMapper.mapToCommentDto(comment);
    }

    public void calculateCountCommentPost(String idPost, boolean delete) {
        PostEntity post = getPostById(idPost);
        int totalCount = post.getCommentsCount();

        if (delete) {
            post.setCommentsCount(totalCount - 1);
            postRepository.save(post);
        } else {
            post.setCommentsCount(totalCount + 1);
            postRepository.save(post);
        }
    }

    public PostEntity getPostById(String id) {
        return postRepository.findPostEntityById(id);
    }

    public CommentEntity getCommentById(String id) {
        return commentRepository.findCommentEntityById(id);
    }

    @Override
    public void createLikeComment(String idPost, String idComment, String headerRequestByAuth) {
        PostEntity post = getPostById(idPost);
        CommentEntity comment = commentRepository.findCommentEntityById(idComment);
        if (post != null || comment != null) {

            calculateCountCommentPost(post.getId(), false);
            createNewLikeComment(comment.getId());
            calculateCountLikeComment(idComment, false);

            try {
                String stringToken = headerRequestByAuth.substring(7);
                DecodedToken token = DecodedToken.getDecoded(stringToken);
                UUID idLikeAuthor = UUID.fromString(token.getId());

                producer.sendMessageForNotification(NotificationDTO.builder()
                        .id(UUID.randomUUID())
                        .authorId(idLikeAuthor)
                        .content("Ваш комментарий одобрили")
                        .notificationType(NotificationType.LIKE_COMMENT)
                        .sentTime(LocalDateTime.now())
                        .receiverId(UUID.fromString(comment.getAuthorId()))
                        .serviceName(MicroServiceName.POST)
                        .build());
            } catch (Exception e) {
                throw new ResourceNotFoundException(MessageFormat.format("Error: {0}", e.getMessage()));
            }
        } else {
            throw new ResourceNotFoundException("Данного поста или комментария не существует!");
        }
    }

    public void calculateCountLikeComment(String idComment, boolean delete) {
        CommentEntity comment = getCommentById(idComment);
        int totalAmountLike = comment.getLikeAmount();
        if (delete) {
            comment.setLikeAmount(totalAmountLike - 1);
            commentRepository.save(comment);
        } else {
            comment.setLikeAmount(totalAmountLike + 1);
            commentRepository.save(comment);
        }
    }

    public LikeEntity createNewLikeComment(String itemId){
        LikeEntity newLike = LikeEntity.builder()
                .time(new Date())
                .type(TypeLike.COMMENT)
                .itemId(itemId)
                .isDeleted(false)
                .reactionType("")
                .build();
        return likeRepository.save(newLike);
    }

    @Override
    public void removeLikeComment(String idPost, String idComment) throws BadRequestException {
        PostEntity post = getPostById(idPost);
        if (post != null) {
            CommentEntity comment = getCommentById(idComment);
            calculateCountLikeComment(comment.getId(), true);
            commentRepository.delete(comment);
        } else {
            throw new BadRequestException("Bad request");
        }
    }

    @Override
    public Page<CommentEntity> getAllComments(String idPost, PageableDto pageableDto,
                                              CommentSearchDto commentSearchDto){

        Specification<CommentEntity> spec = Specification.where(null);

        if (idPost != null){
            spec.and(commentSpecification.findCommentsByPostId(idPost));
        }

        if (commentSearchDto.getCommentType().equals(TypeComment.COMMENT.toString())){
            spec.and(commentSpecification.findCommentsByCommentType(TypeComment.COMMENT));
        }

        if (commentSearchDto.getAuthorId() != null){
            spec.and(commentSpecification.findCommentsByAuthorId(commentSearchDto.getAuthorId()));
        }

        if (commentSearchDto.getPostId() != null){
            spec.and(commentSpecification.findCommentsByPostId(commentSearchDto.getPostId()));
        }

        return commentRepository.findAll(spec, PageRequest.of(pageableDto.getPage(), pageableDto.getSize()));
    }

    @Override
    public Page<CommentEntity> getAllSubComments(String idPost, String commentId,
                                                 PageableDto pageableDto,
                                                 CommentSearchDto commentSearchDto) {
        Specification<CommentEntity> spec = Specification.where(null);

        if (idPost != null){
            spec.and(commentSpecification.findCommentsByPostId(idPost));
        }

        if (commentSearchDto.getCommentType().equals(TypeComment.COMMENT)){
            spec.and(commentSpecification.findCommentsByCommentType(TypeComment.COMMENT));
        }

        if (commentSearchDto.getAuthorId() != null){
            spec.and(commentSpecification.findCommentsByAuthorId(commentSearchDto.getAuthorId()));
        }

        if (commentSearchDto.getPostId() != null){
            spec.and(commentSpecification.findCommentsByPostId(commentSearchDto.getPostId()));
        }

        if (commentSearchDto.getParentId()!= null){
            spec.and(commentSpecification.findCommentsByParentId(commentSearchDto.getParentId()));
        }

        if (commentId != null){
            spec.and(commentSpecification.findCommentsByParentId(commentId));
        }

        return commentRepository.findAll(spec, PageRequest.of(pageableDto.getPage(), pageableDto.getSize()));
    }
}
