package com.social.mc_post.mapper;

import com.social.mc_post.dto.PostDto;
import com.social.mc_post.repository.CommentRepository;
import com.social.mc_post.repository.PostRepository;
import com.social.mc_post.structure.PostEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ConvertToPageMapper {


    private final PostMapper postMapper;
    private final CommentMapper commentMapper;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public Page<PostDto> findPostByDateToDateFrom(Date to, Date from, Pageable pageable){
        Page<PostEntity> entityPage = postRepository.findPageOfPostByPublishDateBetweenOrderByPublishDate(to, from, pageable);
        List<PostDto> dtos = postMapper.mapListToPostDto(entityPage.getContent());
        return new PageImpl<>(dtos, pageable, entityPage.getTotalElements());
    }



}
