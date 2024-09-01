package com.social.mc_post.services;

import com.social.mc_post.dto.*;


public interface PostService {

    PostPageDTO getPosts(PostSearchDto searchDto, Page pageable);
    String createPost(PostDto newPost, String headerRequestByAuth);
    PostDto updatePost(PostDto updatePost);
    void deletePost(String id);
    LikeDto createLikePost(String idPost, LikeDto likeDto, String headerRequestByAuth);
    void deleteLike(String id);
    String delayed(String token);
    PostDto getPostDtoById(String id);
}
