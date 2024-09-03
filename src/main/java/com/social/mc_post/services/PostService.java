package com.social.mc_post.services;

import com.social.mc_post.dto.*;
import org.springframework.data.domain.Page;


public interface PostService {

    Page<PostDto> getPosts(PostSearchDto searchDto, PageDto pageable, String headerRequestByAuth);
    String createPost(PostDto newPost, String headerRequestByAuth);
    PostDto updatePost(PostDto updatePost);
    void deletePost(String id);
    LikeDto createLikePost(String idPost, LikeDto likeDto, String headerRequestByAuth);
    void deleteLike(String id);
    String delayed(String token);
    PostDto getPostDtoById(String id);
}
