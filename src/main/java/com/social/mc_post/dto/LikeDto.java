package com.social.mc_post.dto;

import com.social.mc_post.dto.enums.TypeLike;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikeDto {

    private String id;

    private Boolean isDeleted;

    private String authorId;

    private LocalDateTime time;

    private String itemId;

    private TypeLike type;

    private String reactionType;
}
