package com.social.mc_post.services;

import com.social.mc_post.dto.LikeDto;
import com.social.mc_post.dto.PageableDto;
import com.social.mc_post.dto.PostDto;
import com.social.mc_post.dto.PostSearchDto;
import com.social.mc_post.structure.PostEntity;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PostService {

    List<PostDto> getPosts(PostSearchDto postSearchDto, PageableDto pageableDto, String token);
    void createPost(PostDto newPost, String token);
    void updatePost(PostDto updatePost);
    PostDto getPostById(String id);
    void deletePost(String id);
    void createDeferredPost();
    LikeDto createLikePost(String idPost, LikeDto likeDto);
    Boolean checkPost(String postId);
    void deleteLike(String id) throws BadRequestException;
    Page<PostEntity> getAllPosts(PostSearchDto postSearchDto, PageableDto pageableDto);
    String delayed(String token);
}
