package com.social.mc_post.services.impl;

import com.social.mc_post.dto.*;
import com.social.mc_post.dto.enums.TypeLike;
import com.social.mc_post.dto.enums.TypePost;
import com.social.mc_post.dto.notification.MicroServiceName;
import com.social.mc_post.dto.notification.NotificationDTO;
import com.social.mc_post.dto.notification.NotificationType;
import com.social.mc_post.exception.ResourceNotFoundException;
import com.social.mc_post.feign.AccountClient;
import com.social.mc_post.feign.FriendClient;
import com.social.mc_post.kafka.KafkaProducer;
import com.social.mc_post.mapper.LikeMapper;
import com.social.mc_post.mapper.PostMapper;
import com.social.mc_post.mapper.TagMapper;
import com.social.mc_post.model.Comment;
import com.social.mc_post.model.Like;
import com.social.mc_post.model.Post;
import com.social.mc_post.model.Tag;
import com.social.mc_post.repository.CommentRepository;
import com.social.mc_post.repository.LikeRepository;
import com.social.mc_post.repository.PostRepository;
import com.social.mc_post.repository.TagRepository;
import com.social.mc_post.security.DecodedToken;
import com.social.mc_post.services.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import org.jsoup.Jsoup;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@EnableAsync
@ConditionalOnProperty(name = "scheduler.enabled", matchIfMissing = true)
public class PostServiceImpl implements PostService {
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final TagMapper tagMapper;
    private final PostMapper postMapper;
    private final LikeMapper likeMapper;
    private final KafkaProducer producer;
    private final FriendClient friendClient;
    private final AccountClient accountClient;


    @Override
    public Page<PostDto> getPosts(PostSearchDto searchDto,
                                  PageDto pageDto, String headerRequestByAuth) {
        try {
            Sort sort = Sort.unsorted();
            Pageable pageable;
            if (pageDto.getSort() == null) {
                pageable = PageRequest.of(0, 10, sort);
            } else {
                pageable = PageRequest.of(pageDto.getPage(), pageDto.getSize(), sort);
            }

            List<String> ids = getAuthorIdsFromSearch(headerRequestByAuth, searchDto);
            List<PostDto> posts = getPostDtoByFilterTime(searchDto, ids);

           if (searchDto.getText() != null){
               posts = posts.stream()
                       .filter(postDto -> postDto.getTitle().toLowerCase()
                               .contains(searchDto.getText().toLowerCase()))
                       .toList();
           }

           List<String> tags = searchDto.getTags() == null ? List.of() : searchDto.getTags();
           if (!tags.isEmpty()){
               for (String tag : tags){
                   posts = posts.stream()
                           .filter(postDto -> postDto.getTags().contains(tag))
                           .toList();
               }
           }
           return new PageImpl<>(posts,pageable, pageDto.getSize());
        }catch (Exception e){
            log.error("Error: {}", e.getMessage());
            Sort sort = Sort.unsorted();
            Pageable pageable = PageRequest.of(pageDto.getPage(), pageDto.getSize(), sort);
            return new PageImpl<>(List.of() , pageable, pageDto.getSize());
        }
    }

    @Override
    public String createPost(PostDto postDto, String headerRequestByAuth) {
            try {
                Post post = createPostDB(postDto, headerRequestByAuth);
                List<Tag> tags = createTags(postDto.getTags(), post);
                postRepository.save(post);
                for (Tag tag : tags){
                    tagRepository.save(tag);
                }
                log.info("Create new POST: {}", postDto.getTitle());

                String content = post.getTitle();
                putNotification(UUID.fromString(post.getAuthorId()), content, NotificationType.POST, null);

                return "Post created";
            }catch (Exception e){
                throw new ResourceNotFoundException("Error: " + e.getMessage());
            }
    }

    @Override
    public String updatePost(PostDto postDto, String headerRequestByAuth) {
        Post post = postRepository.findById(postDto.getId()).orElse(null);
        if (!postDto.getTags().isEmpty()){
            List<Tag> tags = createTags(postDto.getTags(), post);
            tags.forEach(tagRepository::save);
        }
        try {
            if (!post.getAuthorId().equals(getAuthorId(headerRequestByAuth))){
                return "You didn't create the post! The post has not been changed!";
            }
        }catch (Exception e){
            throw new ResourceNotFoundException("Error: " + e.getMessage());
        }
        if (post != null){
            post.setTimeChanged(LocalDateTime.now());
            post.setTitle(postDto.getTitle());
            post.setPostText(postDto.getPostText());
            post.setImagePath(postDto.getImagePath());
            post.setPublishDate(postDto.getPublishDate());

            if (postDto.getPublishDate() == null){
                post.setType(TypePost.POSTED);
            } else {
                post.setType(TypePost.QUEUED);
            }

            postRepository.save(post);
            log.info("Update POST: {}", post.getId());
            return "The post has been changed";
        }
       return null;
    }

    @Override
    public void deletePost(String id, String headerRequestByAuth) {
        Post post = postRepository.findById(id).orElse(null);
        try {
            if (post != null && post.getAuthorId().equals(getAuthorId(headerRequestByAuth))){
                List<Comment> comments = commentRepository.findByPost(post);
                comments.forEach(comment -> {
                    List<Like> likes = likeRepository.findByComment(comment);
                    likes.forEach(like -> {
                        likeRepository.delete(like);
                        log.info("delete like comment");
                    });
                    commentRepository.delete(comment);
                    log.info("Delete comment");
                });
                List<Like> likes = likeRepository.findByPost(post);
                likes.forEach( like -> {
                    likeRepository.delete(like);
                    log.info("delete like post");
                });

                postRepository.delete(post);
                log.info("Post delete by id: {}", post.getId());
                List<Tag> tags = tagRepository.findByPost(post);
                tags.forEach(tag -> {
                    tagRepository.delete(tag);
                    log.info("delete tag post");
                });
            }
        }catch (Exception e){
            throw new ResourceNotFoundException("Error: " + e.getMessage());
        }

    }

    @Override
    public LikeDto createLikePost(String idPost, LikeDto likeDto, String headerRequestByAuth) {
        Post post = postRepository.findById(idPost).orElse(null);
        if (post != null){
            try {
                String authorId = getAuthorId(headerRequestByAuth);
                Like oldLike = likeRepository.foundByPostAndAuthorId(post, authorId, TypeLike.POST);
                if (oldLike != null){
                    likeRepository.delete(oldLike);
                }
                Like like = createLike(likeDto, post, authorId);
                likeRepository.save(like);
                log.info("Like for post created");
                putNotification(UUID.fromString(like.getAuthorId()), "Ваш пост одобрили", NotificationType.LIKE_POST, UUID.fromString(post.getAuthorId()));

                if (like.getAuthorId().equals(post.getAuthorId())){
                    post.setMyLike(true);
                    post.setMyReaction(like.getReaction());
                    postRepository.save(post);
                }
                return likeMapper.mapEntityToDto(like);
            }catch (Exception e){
                throw new ResourceNotFoundException("Error: " + e.getMessage());
            }
        }
        return null;
    }


    @Override
    public void deleteLike(String id, String headerRequestByAuth){
        Like like = likeRepository.findById(id).orElse(null);
        try {
            String authorId = getAuthorId(headerRequestByAuth);
            if (like != null && like.getAuthorId().equals(authorId)){
                likeRepository.delete(like);
                log.info("Like deleted");
            }
            if (like.getPost() != null && like.getPost().getAuthorId().equals(authorId)){
                Post post = like.getPost();
                post.setMyLike(false);
                post.setMyReaction(null);
                postRepository.save(post);
            }
        }catch (Exception e){
            throw new ResourceNotFoundException("Error: " + e.getMessage());
        }
    }

    @Override
    public String delayed(String token){
        try {
            List<Post> posts = postRepository.findByTypeAndAuthorId(TypePost.QUEUED,
                    getAuthorId(token));
            int countPostedPost = 0;
            for (Post post : posts){
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

    @Override
    public PostDto getPostDtoById(String id) {
        Post post = postRepository.findById(id).orElse(null);
        if (post != null){
            return postMapper.mapEntityToDto(post);
        }
       return null;
    }

    private List<PostDto> getPostDtoByFilterTime(PostSearchDto searchDto, List<String> ids){
        boolean isDeleted = searchDto.getIsDeleted() != null && searchDto.getIsDeleted();
        List<PostDto> posts = new ArrayList<>();

        if (searchDto.getDateTo() != null && searchDto.getDateFrom() != null){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSz");
            LocalDateTime from = LocalDateTime.parse(searchDto.getDateFrom(), formatter);
            LocalDateTime to = LocalDateTime.parse(searchDto.getDateTo(), formatter);

            if (ids.isEmpty()){
                posts.addAll(postRepository.findAll().stream()
                        .filter(post -> post.getIsDeleted().equals(isDeleted))
                        .filter(post -> post.getType().equals(TypePost.POSTED))
                        .filter(post -> post.getTime().isAfter(from) && post.getTime().isBefore(to))
                        .map(postMapper::mapEntityToDto)
                        .toList());
            }else {
                posts.addAll(postRepository.findByAuthorIdList(ids).stream()
                        .filter(post -> post.getIsDeleted().equals(isDeleted))
                        .filter(post -> post.getType().equals(TypePost.POSTED))
                        .filter(post -> post.getTime().isAfter(from) && post.getTime().isBefore(to))
                        .map(postMapper::mapEntityToDto)
                        .toList());
            }
        }else {
            posts.addAll(postRepository.findByAuthorIdList(ids).stream()
                    .filter(post -> post.getIsDeleted().equals(isDeleted))
                    .filter(post -> post.getType().equals(TypePost.POSTED))
                    .map(postMapper::mapEntityToDto)
                    .toList());
        }
        return posts;
    }

    private List<String> getAuthorIdsFromSearch(String headerRequestByAuth,
                                                PostSearchDto searchDto) throws Exception {
        List<String> ids = new ArrayList<>();
        if (searchDto == null){
            return List.of();
        }

        if (searchDto.getAccountIds() != null){
            ids.addAll(searchDto.getAccountIds());
        }

        if (searchDto.getAuthor() != null){
            searchDto.setWithFriends(false);
            ids.addAll(getIdsByAuthorData(headerRequestByAuth, searchDto));
            log.info(ids.toString());
        }

        boolean withFriends = searchDto.getWithFriends() != null && searchDto.getWithFriends();

        if (searchDto.getAuthor() == null && searchDto.getAccountIds() == null && withFriends
                && searchDto.getDateTo() == null){
            ids.add(getAuthorId(headerRequestByAuth));
        }

        if (withFriends && ids.size() == 1){
            ids.addAll(friendClient.getFriendsIdListByUserId(headerRequestByAuth,ids.get(0))
                    .stream()
                    .map(UUID::toString).toList());
        }
      return ids;
    }

    private List<String> getIdsByAuthorData(String headerRequestByAuth,
                                            PostSearchDto searchDto) throws Exception {
        List<String> ids = new ArrayList<>();
        String[] data = searchDto.getAuthor().trim().split("\\s+");
        if (data.length == 2){
            ids.addAll(accountClient
                    .getListIdsAccounts(headerRequestByAuth, data[0], data[1]).stream()
                    .map(UUID::toString)
                    .toList());
            ids.addAll(accountClient
                    .getListIdsAccounts(headerRequestByAuth, data[1], data[0]).stream()
                    .map(UUID::toString)
                    .toList());
            return ids;
        }
       throw new Exception("Неверный формат ввода автора!");
    }

    private List<Tag> createTags(List<TagDto> tagDtoList, Post post){
        return tagDtoList.stream()
                .map(tagDto -> tagMapper.mapDtoToEntity(tagDto, post))
                .toList();
    }

    private Like createLike(LikeDto likeDto, Post post, String authorId) throws UnsupportedEncodingException {
        return new Like(
                null,
                false,
                authorId,
                LocalDateTime.now(),
                post,
                null,
                TypeLike.POST,
                likeDto.getReactionType());
    }

    private Post createPostDB(PostDto dto, String headerRequestByAuth) throws UnsupportedEncodingException {
        Post post = new Post(
                null,
                false,
                LocalDateTime.now(),
                null,
                getAuthorId(headerRequestByAuth),
                dto.getTitle(),
                null,
                Jsoup.parse(dto.getPostText()).text(),
                false,
                dto.getMyReaction(),
                false,
                dto.getImagePath(),
                dto.getPublishDate());

        if (dto.getPublishDate() == null){
            post.setType(TypePost.POSTED);
        } else {
            post.setType(TypePost.QUEUED);
        }
        return post;
    }

    private String getAuthorId(String headerRequestByAuth) throws UnsupportedEncodingException {
        String stringToken = headerRequestByAuth.substring(7);
        DecodedToken decodedToken = DecodedToken.getDecoded(stringToken);
        return decodedToken.getId();
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
    @Scheduled(fixedRate = 30000)
    public void publishingDeferredPosts() {

        List<Post> postList = postRepository.findByType(TypePost.QUEUED);
        for (Post p : postList) {
            if (p.getPublishDate().isAfter(LocalDateTime.now())) {
                p.setTime(LocalDateTime.now());
                p.setType(TypePost.POSTED);
                p.setPublishDate(null);
                postRepository.save(p);
                log.info("Публикация поста: {}", p.getId());
            }
        }
    }
}
