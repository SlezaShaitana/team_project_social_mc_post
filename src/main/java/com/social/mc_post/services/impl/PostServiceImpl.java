package com.social.mc_post.services.impl;

import com.social.mc_post.dto.LikeDto;
import com.social.mc_post.dto.PageableDto;
import com.social.mc_post.dto.PostDto;
import com.social.mc_post.dto.PostSearchDto;
import com.social.mc_post.dto.mappers.LikeMapper;
import com.social.mc_post.dto.mappers.PostMapper;
import com.social.mc_post.exception.PostNotFoundException;
import com.social.mc_post.repository.LikeRepository;
import com.social.mc_post.repository.PostRepository;
import com.social.mc_post.repository.TagRepository;
import com.social.mc_post.repository.specifications.PostSpecification;
import com.social.mc_post.services.PostService;
import com.social.mc_post.structure.LikeEntity;
import com.social.mc_post.structure.PostEntity;
import com.social.mc_post.structure.TagEntity;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PostServiceImpl implements PostService {
    private final LikeRepository likeRepository;

    private final PostRepository postRepository;
    private final TagRepository tagRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository,
                           LikeRepository likeRepository, TagRepository tagRepository) {
        this.postRepository = postRepository;
        this.likeRepository = likeRepository;
        this.tagRepository = tagRepository;
    }

    @Override
    public Page<PostDto> getPosts(PostSearchDto postSearchDto, PageableDto pageableDto) {
        Pageable pageable = PageRequest.of(0, pageableDto.getPage());
        return postRepository
                .findPageOfPostByPublishDateBetweenOrderByPublishDate
                        (postSearchDto.getDateTo(), postSearchDto.getDateFrom(), pageable).map(PostMapper::mapToPostDto);
    }

    @Override
    public PostDto createPost(PostDto newPost) {
        PostEntity post = PostMapper.mapToPostEntity(newPost);
        saveTagInDB(newPost);
        PostEntity savePost = postRepository.save(post);
        PostDto savePostDto = PostMapper.mapToPostDto(savePost);
        log.info("Create new post id: {}", savePostDto.getId());
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
        PostEntity post = PostMapper.mapToPostEntity(updatePost);
        postRepository.updatePost(post, post.getId());
        log.info("Update POST: {}", post.getId());
        return PostMapper.mapToPostDto(post);
    }

    @Override
    public PostDto getPostById(String id) {
        Optional<PostEntity> post = postRepository.findById(id);
        if (post.isPresent()){
            log.info("GET post id: {}", post.get().getId());
            return PostMapper.mapToPostDto(post.get());
        }
        else {
            throw new PostNotFoundException("Post not found");
        }
    }

    @Override
    public void deletePost(String id) {
        PostEntity post = postRepository.findPostEntityById(id);
        if (post != null) {
            postRepository.delete(post);
            log.info("Post delete by id: {}", post.getId());
        } else {
            throw new PostNotFoundException("Post not found");
        }
    }

    @Override
    public LikeDto createLikePost(String idPost, LikeDto likeDto) {
        PostEntity post = postRepository.findPostEntityById(idPost);
        LikeEntity likePost = LikeMapper.mapToLikeEntity(likeDto);
        likePost.setItemId(post.getId());
        likeRepository.save(likePost);
        return LikeMapper.mapToLikeDto(likePost);
    }

    @Override
    public Boolean checkPost(String postId){
        Optional<PostEntity> post = postRepository.findById(postId);
        return post.isPresent();
    }


    public Page<PostEntity> find(String title, Integer page){
        Specification<PostEntity> spec = Specification.where(null);
        if (title != null){
            spec.and(PostSpecification.getPostByTitle(title));
        }
        return postRepository.findAll(spec, PageRequest.of(page - 1, 5));
    }

}
