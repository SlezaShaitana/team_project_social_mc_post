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
import com.social.mc_post.services.PostService;
import com.social.mc_post.structure.LikeEntity;
import com.social.mc_post.structure.PostEntity;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class PostServiceImpl implements PostService {
    private final LikeRepository likeRepository;

    private final PostRepository postRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository,
                           LikeRepository likeRepository) {
        this.postRepository = postRepository;
        this.likeRepository = likeRepository;
    }

    @Override
    public Page<PostEntity> getPosts(PostSearchDto postSearchDto, PageableDto pageableDto) {
        Pageable pageable = PageRequest.of(0, pageableDto.getPage());
        return postRepository
                .findPageOfPostByPublishDateBetweenOrderByPublishDate
                        (postSearchDto.getDateTo(), postSearchDto.getDateFrom(), pageable);
    }

    @Override
    public PostDto createPost(PostDto newPost) {
        PostEntity post = PostMapper.mapToPostEntity(newPost);
        PostEntity savePost = postRepository.save(post);
        PostDto savePostDto = PostMapper.mapToPostDto(savePost);
        log.info("Create new post id: {}", savePostDto.getId());
        return savePostDto;
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
        //likePost.setItemId(post.getId());
        likeRepository.save(likePost);
        return LikeMapper.mapToLikeDto(likePost);
    }

}
