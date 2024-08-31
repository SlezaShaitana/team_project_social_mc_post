package com.social.mc_post.services;

import com.social.mc_post.dto.LikeDto;
import com.social.mc_post.dto.PageableDto;
import com.social.mc_post.dto.PostDto;
import com.social.mc_post.dto.PostSearchDto;
import com.social.mc_post.structure.PostEntity;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;

public interface PostService {

    Page<PostDto> getPosts(PostSearchDto postSearchDto, PageableDto pageableDto);
    void createPost(PostDto newPost, String token);
    PostDto updatePost(PostDto updatePost);
    PostDto getPostById(String id);
    void deletePost(String id);
    void createDeferredPost();
    LikeDto createLikePost(String idPost, LikeDto likeDto);
    Boolean checkPost(String postId);
    void deleteLike(String id) throws BadRequestException;
    Page<PostEntity> getAllPosts(PostSearchDto postSearchDto, PageableDto pageableDto);
}
