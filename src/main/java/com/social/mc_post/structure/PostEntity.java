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

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    private Date time;

    @Column(name = "time_change")
    private Date timeChanged;

    @Column(name = "author_id")
    private String authorId;

    @Column(name = "title")
    private String title;

    @Enumerated(EnumType.STRING)
    private TypePost type;

    @Column(name = "post_text")
    private String postText;

    @Column(name = "is_blocked")
    private Boolean isBlocked;

    @Column(name = "comments_count")
    private Integer commentsCount;

    @OneToMany
    private List<TagEntity> tags;

    @OneToMany
    private List<ReactionEntity> reactions;

    @Column(name = "my_reaction")
    private String myReaction;

    @Column(name = "time_change", insertable=false, updatable=false)
    private Integer likeAmount;

    private Boolean myLike;

    private String imagePath;

    private Date publishDate;

    public PostEntity(){}

}
