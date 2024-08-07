package com.social.mc_post.services;

import com.social.mc_post.dto.LikeDto;
import com.social.mc_post.dto.PageableDto;
import com.social.mc_post.dto.PostDto;
import com.social.mc_post.dto.PostSearchDto;
import org.springframework.data.domain.Page;

public interface PostService {

    Page<PostDto> getPosts(PostSearchDto postSearchDto, PageableDto pageableDto);
    PostDto createPost(PostDto newPost);
    PostDto updatePost(PostDto updatePost);
    PostDto getPostById(String id);
    void deletePost(String id);
    void createDeferredPost();
    LikeDto createLikePost(String idPost, LikeDto likeDto);
    Boolean checkPost(String postId);
}
