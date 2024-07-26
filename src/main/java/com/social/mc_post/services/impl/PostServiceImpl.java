package com.social.mc_post.services.impl;

import com.social.mc_post.dto.PageableDto;
import com.social.mc_post.dto.PostDto;
import com.social.mc_post.dto.PostSearchDto;
import com.social.mc_post.dto.mappers.PostMapper;
import com.social.mc_post.exception.PostNotFoundException;
import com.social.mc_post.repository.PostRepository;
import com.social.mc_post.services.PostService;
import com.social.mc_post.structure.PostEntity;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
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
    public void deletePost(String id) {
        PostEntity post = postRepository.findPostEntityById(id);
        if (post != null) {
            postRepository.delete(post);
            log.info("Post delete by id: {}", post.getId());
        } else {
            throw new PostNotFoundException("Post not found");
        }
    }
}
