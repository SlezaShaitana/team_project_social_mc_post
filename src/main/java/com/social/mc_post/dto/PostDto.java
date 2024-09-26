package com.social.mc_post.dto;

import com.social.mc_post.dto.enums.TypePost;
<<<<<<< HEAD
import com.social.mc_post.structure.TagEntity;
=======
>>>>>>> 37f25c01f13c20b5d3ef933116c30d9e480c03c4
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

<<<<<<< HEAD
    private List<String> reactions;
=======
    private List<ReactionDto> reactions;
>>>>>>> 37f25c01f13c20b5d3ef933116c30d9e480c03c4

    private String myReaction;

    private Integer likeAmount;

    private Boolean myLike;

    private String imagePath;

    private LocalDateTime publishDate;
}
