package com.social.mc_post.dto;


import com.social.mc_post.dto.enums.TypeComment;
import lombok.*;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDto {

    private String id;

    private Boolean isDeleted;

    private TypeComment commentType;

    private Date time;

    private Date timeChanged;

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
