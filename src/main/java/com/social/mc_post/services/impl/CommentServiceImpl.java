package com.social.mc_post.services.impl;

import com.social.mc_post.dto.CommentDto;
import com.social.mc_post.dto.CommentSearchDto;
import com.social.mc_post.dto.LikeDto;
import com.social.mc_post.dto.PageDto;
import com.social.mc_post.dto.enums.TypeComment;
import com.social.mc_post.dto.enums.TypeLike;
import com.social.mc_post.dto.notification.MicroServiceName;
import com.social.mc_post.dto.notification.NotificationDTO;
import com.social.mc_post.dto.notification.NotificationType;
import com.social.mc_post.exception.ResourceNotFoundException;
import com.social.mc_post.kafka.KafkaProducer;
import com.social.mc_post.mapper.CommentMapper;
import com.social.mc_post.model.Comment;
import com.social.mc_post.model.Like;
import com.social.mc_post.model.Post;
import com.social.mc_post.repository.CommentRepository;
import com.social.mc_post.repository.LikeRepository;
import com.social.mc_post.repository.PostRepository;
import com.social.mc_post.security.DecodedToken;
import com.social.mc_post.services.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final KafkaProducer producer;
    private final CommentMapper commentMapper;


    @Override
    public String createCommentPost(CommentDto commentDto, String postId,String headerRequestByAuth) {
        Post post = postRepository.findById(postId).orElse(null);
        if (post != null){
            try {

                Comment comment = createComment(commentDto, post, headerRequestByAuth);
                if (commentDto.getParentId() != null){
                    comment.setType(TypeComment.COMMENT);
                    comment.setParentCommentId(commentDto.getParentId());
                    commentRepository.save(comment);
                    return "SubComment created";
                }
                comment.setType(TypeComment.POST);
                commentRepository.save(comment);
            }catch (Exception e){
                throw new ResourceNotFoundException("Error: " + e.getMessage());
            }
            return "Comment created";
        }
        return null;
    }

    @Override
    public String createSubCommentPost(String idPost, String idComment,
                                           CommentDto commentDto, String headerRequestByAuth) {
        Post post = postRepository.findById(idPost).orElse(null);
        Comment comment = commentRepository.findById(idComment).orElse(null);
        if (post != null && comment != null){
            try {
                Comment subComment = createComment(commentDto,post,headerRequestByAuth);
                subComment.setType(TypeComment.COMMENT);
                subComment.setParentCommentId(comment.getId());
                commentRepository.save(subComment);
                log.info("Create subComment");
//            putNotification(UUID.fromString(subComment.getAuthorId()),
//            subComment.getCommentText(),NotificationType.COMMENT_COMMENT,
//            UUID.fromString(comment.getAuthorId()));
                return subComment.toString();
            }catch (Exception e){
                throw new ResourceNotFoundException("Error: " + e.getMessage());
            }
        }
        return null;
    }

    @Override
    public void deleteCommentPost(String postId, String commentId, String headerRequestByAuth) {
        Post post = postRepository.findById(postId).orElse(null);
        List<Comment> comments = commentRepository.findByPost(post);
        for (Comment comment : comments){
            try {
                if (comment.getParentCommentId() != null && comment.getParentCommentId().equals(commentId)
                        && comment.getAuthorId().equals(getAuthorId(headerRequestByAuth))){
                    removeLikeComment(postId,commentId, headerRequestByAuth);
                    commentRepository.delete(comment);
                    log.info("SubComment is deleted");
                }
                if (comment.getId().equals(commentId)){
                    removeLikeComment(postId,commentId, headerRequestByAuth);
                    commentRepository.delete(comment);
                    log.info("Comment of post deleted");
                }
            }catch (Exception e){
                throw new ResourceNotFoundException("Error: " + e.getMessage());
            }
        }
    }

    @Override
    public String updateComment(String idPost, CommentDto commentDto, String headerRequestByAuth) {
        Comment comment = commentRepository.findById(commentDto.getId()).orElse(null);
        if (comment != null){
            try {
                if (!comment.getAuthorId().equals(getAuthorId(headerRequestByAuth))){
                    return "Изменять комментарий может только его создатель!";
                }
            }catch (Exception e){
                throw new ResourceNotFoundException("Error: " + e.getMessage());
            }
            comment.setTimeChanged(LocalDateTime.now());
            comment.setCommentText(commentDto.getCommentText());
            comment.setImagePath(commentDto.getImagePath());

            boolean isDeleted = commentDto.getIsDeleted() == null ? comment.getIsDeleted() : commentDto.getIsDeleted();
            boolean isBlocked = commentDto.getIsBlocked() == null ? comment.getIsBlocked() : commentDto.getIsBlocked();
            boolean myLike = commentDto.getMyLike() == null ? comment.getMyLike() : commentDto.getMyLike();

            comment.setIsDeleted(isDeleted);
            comment.setIsBlocked(isBlocked);
            comment.setMyLike(myLike);
            commentRepository.save(comment);
            return "Update comment";
        }
        return null;
    }


    @Override
    public String createLikeComment(String idPost, String idComment,LikeDto likeDto, String headerRequestByAuth) {
        Post post = postRepository.findById(idPost).orElse(null);
        Comment comment = commentRepository.findById(idComment).orElse(null);
        if (post != null && comment != null){
            String authorId = "";
            try {
                authorId = getAuthorId(headerRequestByAuth);
            }catch (Exception e){
                throw new ResourceNotFoundException("Error: " + e.getMessage());
            }
            Like like = createLike(likeDto,post, comment, authorId);
            likeRepository.save(like);
            log.info("Like for comment created ");
            if (like.getAuthorId().equals(like.getAuthorId())){
                comment.setMyLike(true);
                commentRepository.save(comment);
            }
            return "Like for comment created";
        }
       return null;
    }

    @Override
    public void removeLikeComment(String idPost, String idComment, String headerRequestByAuth){
        Comment comment = commentRepository.findById(idComment).orElse(null);
        if (comment != null){
            List<Like> likes = likeRepository.findByComment(comment);
            likes.forEach(like -> {
                try {
                    if (like.getAuthorId().equals(getAuthorId(headerRequestByAuth))){
                        likeRepository.delete(like);
                        log.info("Like for comment deleted");
                    }
                }catch (Exception e){
                    throw new ResourceNotFoundException("Error: " + e.getMessage());
                }
            });
        }

    }

    @Override
    public Page<CommentDto> getCommentsByPostId(CommentSearchDto searchDto, PageDto pageableDto) {
        Post post = postRepository.findById(searchDto.getPostId()).orElse(null);
        List<CommentDto> comments = new ArrayList<>();
        if (post !=null){
            comments.addAll(commentRepository.findByPost(post).stream()
                    .map(commentMapper::mapEntityToDto)
                    .toList());
        }
        Sort sort = Sort.unsorted();

        Pageable pageable;
        if (pageableDto.getSort() == null) {
            pageable = PageRequest.of(0, 10, sort);
        } else {
            pageable = PageRequest.of(pageableDto.getPage(), pageableDto.getSize(), sort);
        }
        return new PageImpl<>(comments,pageable, pageableDto.getSize());
    }

    @Override
    public Page<CommentDto> getSubCommentsByPostIdAndCommentId(CommentSearchDto searchDto, PageDto pageableDto) {
        Post post = postRepository.findById(searchDto.getPostId()).orElse(null);
        Comment comment = commentRepository.findById(searchDto.getParentId()).orElse(null);
        List<CommentDto> comments = new ArrayList<>();
        if (post != null && comment != null){
            comments.addAll(commentRepository.findByParentCommentId(searchDto.getParentId()).stream()
                    .map(commentMapper::mapEntityToDto)
                    .toList());
        }
        Sort sort = Sort.unsorted();
        log.info(comments.toString());
        Pageable pageable;
        if (pageableDto.getSort() == null) {
            pageable = PageRequest.of(0, 10, sort);
        } else {
            pageable = PageRequest.of(pageableDto.getPage(), pageableDto.getSize(), sort);
        }
        return new PageImpl<>(comments,pageable, pageableDto.getSize());
    }


    private Like createLike(LikeDto likeDto, Post post, Comment comment, String authorId){
        return new Like(
                null,
                false,
                authorId,
                LocalDateTime.now(),
                post,
                comment,
                TypeLike.COMMENT,
                likeDto.getReactionType());
    }

    private void putNotification(UUID authorId, String content, NotificationType type, UUID receiverId) {
        producer.sendMessageForNotification(NotificationDTO.builder()
                .id(UUID.randomUUID())
                .authorId(authorId)
                .content(content)
                .notificationType(type)
                .sentTime(LocalDateTime.now())
                .receiverId(receiverId)
                .serviceName(MicroServiceName.POST)
                .build());
    }


    private Comment createComment(CommentDto commentDto,
                                  Post post, String headerRequestByAuth) throws UnsupportedEncodingException {
        return new Comment(
                null,
                false,
                null,
                LocalDateTime.now().plusHours(3),
                null,
                getAuthorId(headerRequestByAuth),
                null,
                commentDto.getCommentText(),
                post,
                false,
                false,
                commentDto.getImagePath());
    }


    private String getAuthorId(String headerRequestByAuth) throws UnsupportedEncodingException {
        String stringToken = headerRequestByAuth.substring(7);
        DecodedToken decodedToken = DecodedToken.getDecoded(stringToken);
        return decodedToken.getId();
    }
}
