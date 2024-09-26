package com.social.mc_post.services;

<<<<<<< HEAD
import com.social.mc_post.dto.LikeDto;
import com.social.mc_post.dto.PageableDto;
import com.social.mc_post.dto.PostDto;
import com.social.mc_post.dto.PostSearchDto;
import com.social.mc_post.structure.PostEntity;
import org.apache.coyote.BadRequestException;
=======
import com.social.mc_post.dto.*;
>>>>>>> 37f25c01f13c20b5d3ef933116c30d9e480c03c4
import org.springframework.data.domain.Page;


public interface PostService {

<<<<<<< HEAD
    Page<PostDto> getPosts(PostSearchDto postSearchDto, PageableDto pageableDto);
    void createPost(PostDto newPost, String token);
    void updatePost(PostDto updatePost);
    PostDto getPostById(String id);
    void deletePost(String id);
    void createLikePost(String idPost, LikeDto likeDto, String tokenAuth);
    Boolean checkPost(String postId);
    void deleteLike(String id) throws BadRequestException;
    Page<PostEntity> getAllPosts(PostSearchDto postSearchDto, PageableDto pageableDto);
    String delayed(String token);
=======
    Page<PostDto> getPosts(PostSearchDto searchDto, PageDto pageable, String headerRequestByAuth);
    String createPost(PostDto newPost, String headerRequestByAuth);
    String updatePost(PostDto updatePost, String headerRequestByAuth);
    void deletePost(String id, String headerRequestByAuth);
    LikeDto createLikePost(String idPost, LikeDto likeDto, String headerRequestByAuth);
    void deleteLike(String id, String headerRequestByAuth);
    String delayed(String token);
    PostDto getPostDtoById(String id);
>>>>>>> 37f25c01f13c20b5d3ef933116c30d9e480c03c4
}
