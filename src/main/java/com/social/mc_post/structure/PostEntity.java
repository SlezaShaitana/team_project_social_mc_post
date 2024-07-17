package com.social.mc_post.structure;

import com.social.mc_post.dto.ReactionDto;
import com.social.mc_post.dto.TagDto;
import com.social.mc_post.dto.enums.TypePost;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "post")
@Setter
@Getter
@Builder
@AllArgsConstructor
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private Boolean isDeleted;

    private Date time;

    private Date timeChanged;

    private String authorId;

    private String title;

    @Enumerated(EnumType.STRING)
    private TypePost type;

    private String postText;

    private Boolean isBlocked;

    private Integer commentsCount;

    @OneToMany
    private List<TagEntity> tags;

    @OneToMany
    private List<ReactionEntity> reactions;

    private String myReaction;

    private Integer likeAmount;

    private Boolean myLike;

    private String imagePath;

    private Date publishDate;

    public PostEntity(){}

}
