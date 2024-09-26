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
import com.social.mc_post.mapper.LikeMapper;
import com.social.mc_post.model.Comment;
import com.social.mc_post.model.Like;
import com.social.mc_post.model.Post;
import com.social.mc_post.repository.CommentRepository;
import com.social.mc_post.repository.LikeRepository;
import com.social.mc_post.repository.PostRepository;
<<<<<<< HEAD
import com.social.mc_post.repository.specifications.CommentSpecification;
=======
import com.social.mc_post.security.DecodedToken;
>>>>>>> 37f25c01f13c20b5d3ef933116c30d9e480c03c4
import com.social.mc_post.services.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
<<<<<<< HEAD
import java.util.Date;
import java.util.List;
import java.util.Optional;
=======
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
>>>>>>> 37f25c01f13c20b5d3ef933116c30d9e480c03c4
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
    private final LikeMapper likeMapper;


    @Override
<<<<<<< HEAD
    public void createCommentPost(CommentDto commentDto, String postId, String token) {

        Optional<PostEntity> post = postRepository.findById(postId);
        if (post.isPresent()){
            CommentEntity newComment = CommentEntity
                    .builder()
                    .commentsCount(0)
                    .commentText(commentDto.getCommentText())
                    .imagePath(commentDto.getImagePath())
                    .authorId(GettingDataService.getUserIdFromToken(token))
                    .isDeleted(false)
                    .isBlocked(false)
                    .parentId(postId)
                    .time(new Date())
                    .likeAmount(0)
                    .post(post.get())
                    .timeChanged(commentDto.getTimeChanged())
                    .build();
            calculateCountCommentPost(postId, false);
            commentRepository.save(newComment);
            log.info("Comment id : {}", newComment.getId());

            producer.sendMessageForNotification(NotificationDTO.builder()
                    .id(UUID.randomUUID())
                    .authorId(UUID.fromString(newComment.getAuthorId()))
                    .content(newComment.getCommentText())
                    .notificationType(NotificationType.POST_COMMENT)
                    .sentTime(LocalDateTime.now())
                    .receiverId(UUID.fromString(post.get().getAuthorId()))
                    .serviceName(MicroServiceName.POST)
                    .build());
        } else {
            throw new ResourceNotFoundException("Пост не найден");
        }
    }

    @Override
    public void createSubCommentPost(String idPost, String idComment, CommentDto subComment, String token) {
        Optional<PostEntity> post = postRepository.findById(idPost);

        if (post.isPresent()) {

            CommentEntity commentPost = commentRepository.findCommentEntityById(idComment);

            CommentEntity newSubComment = CommentEntity
                    .builder()
                    .commentText(subComment.getCommentText())
                    .commentsCount(0)
                    .parentId(commentPost.getId())
                    .isBlocked(false)
                    .authorId(GettingDataService.getUserIdFromToken(token))
                    .imagePath(subComment.getImagePath())
                    .likeAmount(0)
                    .time(new Date())
                    .isDeleted(false)
                    .myLike(false)
                    .build();

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
    public void updateComment(String idComment, CommentDto commentDto) {
        Optional<CommentEntity> comment = commentRepository.findById(idComment);

        if (comment.isPresent()){
            comment.get().setImagePath(commentDto.getImagePath());
            comment.get().setCommentText(commentDto.getCommentText());
            comment.get().setTimeChanged(new Date());
            commentRepository.updateComment(comment.get(), idComment);
        } else {
            throw new ResourceNotFoundException("Данный комментарий не найден");
        }
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
            createNewLikeComment(comment.getId(), headerRequestByAuth);
            calculateCountLikeComment(idComment, false);

            try {

                UUID idLikeAuthor = UUID.fromString(GettingDataService.getUserIdFromToken(headerRequestByAuth));
=======
    public String createCommentPost(CommentDto commentDto, String postId,String headerRequestByAuth) {
        Post post = postRepository.findById(postId).orElse(null);
        if (post != null){
            try {
>>>>>>> 37f25c01f13c20b5d3ef933116c30d9e480c03c4

                Comment comment = createComment(commentDto, post, headerRequestByAuth);
                if (commentDto.getParentId() != null){
                    Comment parentComment = commentRepository.findById(commentDto.getParentId()).orElse(null);
                    if (parentComment != null){
                        comment.setType(TypeComment.COMMENT);
                        comment.setParentCommentId(commentDto.getParentId());
                        commentRepository.save(comment);

                        putNotification(UUID.fromString(comment.getAuthorId()),
                                comment.getCommentText(),NotificationType.COMMENT_COMMENT,
                                UUID.fromString(parentComment.getAuthorId()));
                        return "SubComment created";
                    }
                    return null;
                }
                comment.setType(TypeComment.POST);
                commentRepository.save(comment);
                putNotification(UUID.fromString(comment.getAuthorId()),
                        comment.getCommentText(), NotificationType.POST_COMMENT,
                        UUID.fromString(post.getAuthorId()));
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
                putNotification(UUID.fromString(subComment.getAuthorId()),
                        subComment.getCommentText(),NotificationType.COMMENT_COMMENT,
                        UUID.fromString(comment.getAuthorId()));
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
        Comment comment = commentRepository.findById(commentId).orElse(null);
        try {
            String authorId = getAuthorId(headerRequestByAuth);
            if (post != null && comment != null && comment.getAuthorId().equals(authorId)){
                commentRepository.delete(comment);
                log.info("Comment of post deleted");
                List<Comment> comments = commentRepository.findByParentCommentId(commentId);
                for (Comment com : comments){
                    commentRepository.delete(com);
                    log.info("SubComment is deleted");
                }
            }
        }catch (Exception e){
            throw new ResourceNotFoundException("Error: " + e.getMessage());
        }



    }

    @Override
    public String updateComment(String idPost, CommentDto commentDto, String headerRequestByAuth) {
        Comment comment = commentRepository.findById(commentDto.getId()).orElse(null);
        if (comment != null){
            try {
                if (comment.getAuthorId().equals(getAuthorId(headerRequestByAuth))){
                    return "You didn't create the comment! The post has not been changed!";
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
    public LikeDto createLikeComment(String idPost, String idComment,
                                    String headerRequestByAuth) {
        Post post = postRepository.findById(idPost).orElse(null);
        Comment comment = commentRepository.findById(idComment).orElse(null);
        try {
           String authorId = getAuthorId(headerRequestByAuth);
           if (post != null && comment != null){
               Like oldLike = likeRepository.findByCommentAndAuthorId(comment, authorId, TypeLike.COMMENT);
               if (oldLike != null){
                   return likeMapper.mapEntityToDto(oldLike);
               }
               Like like = createLike(post, comment, authorId);
               likeRepository.save(like);
               log.info("Like for comment created ");
               putNotification(UUID.fromString(like.getAuthorId()),
                       "Ваш комментарий одобрили",
                       NotificationType.LIKE_COMMENT,
                       UUID.fromString(comment.getAuthorId()));
               if (comment.getAuthorId().equals(authorId)){
                   comment.setMyLike(true);
                   commentRepository.save(comment);
               }
               return likeMapper.mapEntityToDto(like);
           }
        }catch (Exception e){
            log.info(Arrays.toString(e.getStackTrace()));
            throw new ResourceNotFoundException("Error: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void removeLikeComment(String idPost, String idComment, String headerRequestByAuth){
        Comment comment = commentRepository.findById(idComment).orElse(null);
        try {
            String authorId = getAuthorId(headerRequestByAuth);
            if (comment != null && comment.getAuthorId().equals(authorId)){
                List<Like> likes = likeRepository.findByComment(comment);
                likes.forEach(like -> {
                    likeRepository.delete(like);
                    log.info("Like for comment deleted");
                    if (like.getComment().getAuthorId().equals(authorId)){
                        Comment com = like.getComment();
                        com.setMyLike(false);
                        commentRepository.save(com);
                    }
                });
            }

        }catch (Exception e){
            throw new ResourceNotFoundException("Error: " + e.getMessage());
        }
    }

    @Override
    public Page<CommentDto> getCommentsByPostId(CommentSearchDto searchDto, PageDto pageableDto) {
        Post post = postRepository.findById(searchDto.getPostId()).orElse(null);
        List<CommentDto> comments = new ArrayList<>();
        if (post !=null){
            comments.addAll(commentRepository.findByPost(post).stream()
                    .filter(comment -> comment.getParentCommentId() == null)
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
<<<<<<< HEAD
    }

    public LikeEntity createNewLikeComment(String itemId, String token){
        LikeEntity newLike = LikeEntity.builder()
                .time(new Date())
                .type(TypeLike.COMMENT)
                .itemId(itemId)
                .isDeleted(false)
                .authorId(GettingDataService.getUserIdFromToken(token))
                .reactionType("")
                .build();
        return likeRepository.save(newLike);
=======
        return new PageImpl<>(comments,pageable, pageableDto.getSize());
>>>>>>> 37f25c01f13c20b5d3ef933116c30d9e480c03c4
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


<<<<<<< HEAD
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
=======
    private Like createLike(Post post, Comment comment, String authorId){
        return new Like(
                null,
                false,
                authorId,
                LocalDateTime.now(),
                post,
                comment,
                TypeLike.COMMENT,
                null);
>>>>>>> 37f25c01f13c20b5d3ef933116c30d9e480c03c4
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


    @Override
    public Page<CommentEntity> getCommentsPost(String postId, PageableDto page, CommentSearchDto searchDto){
        PostEntity post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post not found!"));
        Sort sort = Sort.by("time");
        Pageable pageable = PageRequest.of(page.getSize(), page.getPage(), sort);

        return commentRepository.findByCommentTypeAndPostId(TypeComment.POST, post.getId(), pageable);

    }
}
