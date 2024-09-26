package com.social.mc_post.dto;


import com.social.mc_post.dto.enums.TypeComment;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDto {

    private String id;

    private Boolean isDeleted;

    private TypeComment commentType;

    private LocalDateTime time;

    private LocalDateTime timeChanged;

    private String authorId;

    private String parentId;

    private String commentText;

    private String postId;

    private Boolean isBlocked;

    private Integer likeAmount;

    private Boolean myLike;

    private Integer commentsCount;

    private String imagePath;
}
