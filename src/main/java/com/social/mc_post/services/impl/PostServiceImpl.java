package com.social.mc_post.services.impl;

import com.social.mc_post.dto.LikeDto;
import com.social.mc_post.dto.PageableDto;
import com.social.mc_post.dto.PostDto;
import com.social.mc_post.dto.PostSearchDto;
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
import com.social.mc_post.services.PostService;
import com.social.mc_post.structure.LikeEntity;
import com.social.mc_post.structure.PostEntity;
import com.social.mc_post.structure.TagEntity;
import jakarta.resource.spi.AuthenticationMechanism;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    @Autowired
    public PostServiceImpl(LikeRepository likeRepository,
                           PostRepository postRepository,
                           TagRepository tagRepository,
                           LikeMapper likeMapper,
                           KafkaProducer producer) {
        this.likeRepository = likeRepository;
        this.postRepository = postRepository;
        this.tagRepository = tagRepository;
        this.likeMapper = likeMapper;
        this.producer = producer;
    }

    private final KafkaProducer producer;


    @Override
    public Page<PostDto> getPosts(PostSearchDto postSearchDto, PageableDto pageableDto) {
        Pageable pageable = PageRequest.of(pageableDto.getPage(), pageableDto.getSize());
        return postRepository
                .findPageOfPostByPublishDateBetweenOrderByPublishDate
                        (postSearchDto.getDateTo(), postSearchDto.getDateFrom(), pageable).map(PostMapper.MAPPER::mapToPostDto);
    }

    @Override
    public PostDto createPost(PostDto newPost) {
        PostEntity post = PostMapper.MAPPER.mapToPostEntity(newPost);
        saveTagInDB(newPost);
        PostEntity savePost = postRepository.save(post);
        PostDto savePostDto = PostMapper.MAPPER.mapToPostDto(savePost);
        log.info("Create new post id: {}", savePostDto.getId());
        //putNotificationAboutPost(post);
        return savePostDto;
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
    public PostDto updatePost(PostDto updatePost) {
        PostEntity post = PostMapper.MAPPER.mapToPostEntity(updatePost);
        postRepository.updatePost(post, post.getId());
        log.info("Update POST: {}", post.getId());
        return PostMapper.MAPPER.mapToPostDto(post);
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
        //putNotificationAboutLike(post, likePost);
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


    public Page<PostEntity> find(String title, Integer page){
        Specification<PostEntity> spec = Specification.where(null);
        if (title != null){
            spec.and(PostSpecification.getPostByTitle(title));
        }
        return postRepository.findAll(spec, PageRequest.of(page - 1, 5));
    }

    public void putNotificationAboutPost(PostEntity post) {
        String titlePost = (char)27 + "[1m" + post.getTitle();

        producer.sendMessageForNotification(NotificationDTO.builder()
                .id(UUID.randomUUID())
                .authorId(UUID.fromString(post.getAuthorId()))
                .content(titlePost + "\n" + post.getPostText())
                .notificationType(NotificationType.POST)
                .sentTime(LocalDateTime.now())
                .receiverId(null)
                .serviceName(MicroServiceName.POST)
                .build());
    }

    public void putNotificationAboutLike(PostEntity post, LikeEntity likePost) {
        producer.sendMessageForNotification(NotificationDTO.builder()
                .id(UUID.randomUUID())
                .authorId(UUID.fromString(likePost.getAuthorId()))
                .content("Ваш пост одобрили!")
                .notificationType(NotificationType.LIKE_POST)
                .sentTime(LocalDateTime.now())
                .receiverId(UUID.fromString(post.getAuthorId()))
                .serviceName(MicroServiceName.POST)
                .build());
    }

}
