package com.social.mc_post.services;

import com.social.mc_post.dto.LikeDto;
import com.social.mc_post.dto.PageableDto;
import com.social.mc_post.dto.PostDto;
import com.social.mc_post.dto.PostSearchDto;
import com.social.mc_post.structure.PostEntity;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface PostService {

    Page<PostEntity> getPosts(PostSearchDto postSearchDto, PageableDto pageableDto);
    PostDto createPost(PostDto newPost);
    PostDto updatePost(PostDto updatePost);
    PostDto getPostById(String id);
    void deletePost(String id);

    LikeDto createLikePost(String idPost, LikeDto likeDto);
    Boolean checkPost(String postId);
}
