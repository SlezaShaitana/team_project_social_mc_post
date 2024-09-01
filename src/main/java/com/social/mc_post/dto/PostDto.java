package com.social.mc_post.dto;

import com.social.mc_post.dto.enums.TypePost;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@Builder
public class PostDto {

    private String id;

    private Boolean isDeleted;

    private LocalDateTime time;

    private LocalDateTime timeChanged;

    private String authorId;

    private String title;

    private TypePost type;

    private String postText;

    private Boolean isBlocked;

    private Integer commentsCount;

    private List<TagDto> tags;

    private List<ReactionDto> reactions;

    private String myReaction;

    private Integer likeAmount;

    private Boolean myLike;

    private String imagePath;

    private LocalDateTime publishDate;
}
