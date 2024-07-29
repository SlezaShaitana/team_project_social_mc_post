package com.social.mc_post.dto;

import com.social.mc_post.dto.enums.TypeLike;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikeDto {

    private String id;

    private Boolean isDeleted;

    private String authorId;

    private Date time;

    private String itemId;

    private TypeLike type;

    private String reactionType;
}
