package com.social.mc_post.services;

import com.social.mc_post.dto.*;
import org.springframework.data.domain.Page;


public interface PostService {

    Page<PostDto> getPosts(PostSearchDto searchDto, PageDto pageable, String headerRequestByAuth);
    String createPost(PostDto newPost, String headerRequestByAuth);
    String updatePost(PostDto updatePost, String headerRequestByAuth);
    void deletePost(String id, String headerRequestByAuth);
    LikeDto createLikePost(String idPost, LikeDto likeDto, String headerRequestByAuth);
    void deleteLike(String id, String headerRequestByAuth);
    String delayed(String token);
    PostDto getPostDtoById(String id);
}
