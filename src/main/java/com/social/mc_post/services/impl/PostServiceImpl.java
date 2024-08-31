package com.social.mc_post.services.impl;

import com.social.mc_post.dto.LikeDto;
import com.social.mc_post.dto.PageableDto;
import com.social.mc_post.dto.PostDto;
import com.social.mc_post.dto.PostSearchDto;
import com.social.mc_post.dto.enums.TypePost;
import com.social.mc_post.dto.notification.MicroServiceName;
import com.social.mc_post.dto.notification.NotificationDTO;
import com.social.mc_post.dto.notification.NotificationType;
import com.social.mc_post.exception.ResourceNotFoundException;
import com.social.mc_post.kafka.KafkaProducer;
import com.social.mc_post.mapper.LikeMapper;
import com.social.mc_post.mapper.PostMapper;
import com.social.mc_post.repository.LikeRepository;
import com.social.mc_post.repository.PostRepository;
import com.social.mc_post.repository.TagRepository;
import com.social.mc_post.repository.specifications.PostSpecification;
import com.social.mc_post.security.DecodedToken;
import com.social.mc_post.services.PostService;
import com.social.mc_post.structure.LikeEntity;
import com.social.mc_post.structure.PostEntity;
import com.social.mc_post.structure.TagEntity;
import jakarta.resource.spi.AuthenticationMechanism;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class PostServiceImpl implements PostService {
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final LikeMapper likeMapper;
    private final PostMapper postMapper;
    private final KafkaProducer producer;

    @Autowired
    public PostServiceImpl(LikeRepository likeRepository,
                           PostRepository postRepository,
                           TagRepository tagRepository,
                           LikeMapper likeMapper, PostMapper postMapper,
                           KafkaProducer producer) {
        this.likeRepository = likeRepository;
        this.postRepository = postRepository;
        this.tagRepository = tagRepository;
        this.likeMapper = likeMapper;
        this.postMapper = postMapper;
        this.producer = producer;
    }

    @Override
    public Page<PostDto> getPosts(PostSearchDto postSearchDto, PageableDto pageableDto) {
        return getAllPosts(postSearchDto, pageableDto).map(postMapper::mapToPostDto);
    }

    @Override
    public void createPost(PostDto newPost, String token) {
            try {
                saveTagInDB(newPost);
                PostEntity postEntity = savePostDB(newPost, token);

                String titlePost = (char)27 + "[1m" + postEntity.getTitle();
                String content = titlePost + "\n" + postEntity.getPostText();
                // putNotification(UUID.fromString(postEntity.getAuthorId()), content, NotificationType.POST, null);
            }catch (Exception e){
                throw new ResourceNotFoundException("Error: " + e.getMessage());
            }
    }

    public PostEntity savePostDB(PostDto dto, String tokenAuth) throws UnsupportedEncodingException {
        PostEntity postEntity = PostEntity
                .builder()
                .imagePath(dto.getImagePath())
                .postText(dto.getPostText())
                .publishDate(dto.getPublishDate())
                .tags(dto.getTags())
                .title(dto.getTitle())
                .commentsCount(0)
                .isBlocked(false)
                .isDeleted(false)
               // .deferred(false)
                .likeAmount(0)
                .myLike(false)
                .myReaction("")
                .reactions(null)
                .time(new Date())
                .authorId(getUserIdFromToken(tokenAuth))
                .build();

        if (dto.getPublishDate() == null){
            postEntity.setType(TypePost.POSTED);
        } else {
            postEntity.setType(TypePost.QUEUED);
        }

        postRepository.save(postEntity);
        return postEntity;
    }

    public String getUserIdFromToken(String userToken) throws UnsupportedEncodingException {
        String stringToken = userToken.substring(7);
        DecodedToken decodedToken = DecodedToken.getDecoded(stringToken);
        UUID idAuthor = UUID.fromString(decodedToken.getId());
        return idAuthor.toString();
    }

    public void saveTagInDB(PostDto postDto){
        List<TagEntity> tags = postDto.getTags();
        for (TagEntity tag : tags){
            TagEntity newTag = TagEntity.builder()
                    .name(tag.getName())
                    .isDeleted(tag.getIsDeleted())
                    .build();
            tagRepository.save(newTag);
        }
    }

    @Override
    @Transactional
    public void updatePost(PostDto updatePost) {
        Optional<PostEntity> postEntity = postRepository.findById(updatePost.getId());

        if (postEntity.isPresent()){
            postEntity.get().setTitle(updatePost.getTitle());
            postEntity.get().setPostText(updatePost.getPostText());
            postEntity.get().setImagePath(updatePost.getImagePath());
            postEntity.get().setTimeChanged(LocalDateTime.now());
            postRepository.save(postEntity.get());
        } else {
            throw new ResourceNotFoundException("Данный пост отсутствует");
        }

        log.info("Update POST: {}", postEntity.get().getId());

    }

    @Override
    public PostDto getPostById(String id) {
        Optional<PostEntity> post = postRepository.findById(id);
        if (post.isPresent()){
            log.info("GET post id: {}", post.get().getId());
            return PostMapper.MAPPER.mapToPostDto(post.get());
        }
        else {
            throw new ResourceNotFoundException("Пост не найден");
        }
    }

    @Override
    public void deletePost(String id) {
        PostEntity post = postRepository.findPostEntityById(id);
        if (post != null) {
            postRepository.delete(post);
            log.info("Post delete by id: {}", post.getId());
        } else {
            throw new ResourceNotFoundException("Пост не найден");
        }
    }

    @Override
    public void createDeferredPost() {

    }

    @Override
    public LikeDto createLikePost(String idPost, LikeDto likeDto) {
        PostEntity post = postRepository.findPostEntityById(idPost);
        LikeEntity likePost = likeMapper.mapToLikeEntity(likeDto);
        likePost.setItemId(post.getId());
        likeRepository.save(likePost);

       // putNotification(UUID.fromString(likePost.getAuthorId()), "Ваш пост одобрили", NotificationType.LIKE_POST, UUID.fromString(post.getAuthorId()));
        return likeMapper.mapToLikeDto(likePost);
    }

    @Override
    public Boolean checkPost(String postId){
        Optional<PostEntity> post = postRepository.findById(postId);
        return post.isPresent();
    }

    @Override
    public void deleteLike(String id) throws BadRequestException {
        Optional<LikeEntity> like = likeRepository.findById(id);
        if (like.isPresent()){
            likeRepository.delete(like.get());
        } else {
            throw new BadRequestException("Like not found.");
        }
    }

    @Override
    public Page<PostEntity> getAllPosts(PostSearchDto postSearchDto, PageableDto pageableDto){
        Specification<PostEntity> spec = Specification.where(null);

        if (postSearchDto.getAuthor() != null){
            spec.and(PostSpecification.getPostByAuthor(postSearchDto.getAuthor()));
        }

        if (postSearchDto.getDateTo() != null && postSearchDto.getDateFrom() != null){
            spec.and(PostSpecification.getPostsByPublishDateBetween(postSearchDto.getDateTo(), postSearchDto.getDateFrom()));
        }
        return postRepository.findAll(spec, PageRequest.of(pageableDto.getPage(), pageableDto.getSize()));
    }

    @Override
    public String delayed(String token){
        try {
            List<PostEntity> posts = postRepository.findByTypeAndAuthorId(TypePost.QUEUED,
                    getUserIdFromToken(token));
            int countPostedPost = 0;
            for (PostEntity post : posts){
                if (post.getPublishDate().isAfter(LocalDateTime.now())){
                    countPostedPost++;
                    post.setType(TypePost.POSTED);
                    postRepository.save(post);
                }
            }
            return "Published " + countPostedPost + " posts \n " +
                    "The count remaining deferred posts: " + (posts.size() - countPostedPost);
        }catch (Exception e){
            throw new ResourceNotFoundException("Error: " + e.getMessage());
        }
    }

    public void putNotification(UUID authorId, String content, NotificationType type, UUID receiverId) {
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


}
