package com.social.mc_post.services;

import com.social.mc_post.dto.LikeDto;
import com.social.mc_post.dto.PageableDto;
import com.social.mc_post.dto.PostDto;
import com.social.mc_post.dto.PostSearchDto;

import java.util.List;

public interface PostService {

    List<PostDto> getPosts(PageableDto pageableDto, String headerRequestByAuth);
    String createPost(PostDto newPost, String headerRequestByAuth);
    String updatePost(PostDto updatePost);
    void deletePost(String id);
    String createLikePost(String idPost, LikeDto likeDto, String headerRequestByAuth);
    void deleteLike(String id);
    String delayed(String token);
    PostDto getPostDtoById(String id);
}
