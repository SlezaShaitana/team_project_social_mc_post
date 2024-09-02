package com.social.mc_post.structure;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.social.mc_post.dto.enums.TypePost;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.cglib.core.Local;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "posts")
@Setter
@Getter
@Builder
@AllArgsConstructor
public class PostEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    private Date time;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "time_changed")
    private LocalDateTime timeChanged;

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

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "post", orphanRemoval = true)
    private List<TagEntity> tags = new ArrayList<>();

    @Column(name = "my_reaction")
    private String myReaction;

    @Column(name = "like_amount")
    private Integer likeAmount;

    @Column(name = "my_like")
    private Boolean myLike;

    @Column(name = "image_path")
    private String imagePath;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "publish_date")
    private LocalDateTime publishDate;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "post", orphanRemoval = true)
    private List<CommentEntity> commentEntities = new ArrayList<>();

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "post", orphanRemoval = true)
    private List<LikeEntity> likeEntityList;

    public PostEntity(){}

}
