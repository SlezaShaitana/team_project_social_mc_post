package com.social.mc_post.dto;

import com.social.mc_post.dto.enums.TypePost;
import com.social.mc_post.structure.ReactionEntity;
import com.social.mc_post.structure.TagEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@Builder
public class PostDto {

    private String id;

    private Boolean isDeleted;

    private Date time;

    private LocalDateTime timeChanged;

    private String authorId;

    private String title;

    private TypePost type;

    private String postText;

    private Boolean isBlocked;

    private Integer commentsCount;

    private List<TagEntity> tags;

    private List<ReactionEntity> reactions;

    private String myReaction;

    private Integer likeAmount;

    private Boolean myLike;

    private String imagePath;

    private LocalDateTime publishDate;
}
