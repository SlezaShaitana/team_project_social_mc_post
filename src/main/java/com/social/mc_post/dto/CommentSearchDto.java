package com.social.mc_post.dto;

import com.social.mc_post.dto.enums.TypeComment;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentSearchDto {

    private String id;

    private Boolean isDeleted;

    private TypeComment commentType;

    private String authorId;

    private String parentId;

    private String postId;
}
