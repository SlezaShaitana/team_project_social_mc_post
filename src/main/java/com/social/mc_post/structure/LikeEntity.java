package com.social.mc_post.structure;

import com.social.mc_post.dto.enums.TypeLike;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Entity
@Table(name = "like")
@Setter
@Getter
public class LikeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private Boolean isDeleted;

    private String authorId;

    private Date time;

    private String itemId;

    @Enumerated(EnumType.STRING)
    private TypeLike type;

    private String reactionType;
}
