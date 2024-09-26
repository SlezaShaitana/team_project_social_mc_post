package com.social.mc_post.model;

import com.social.mc_post.dto.enums.TypePost;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "time_created", nullable = false)
    private LocalDateTime time;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "time_changed")
    private LocalDateTime timeChanged;

    @Column(name = "author_id", nullable = false)
    private String authorId;

    @Column(name = "title")
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypePost type;

    @Column(name = "post_text")
    private String postText;

    @Column(name = "is_blocked", nullable = false)
    private Boolean isBlocked;

    @Column(name = "my_reaction")
    private String myReaction;

    @Column(name = "my_like", nullable = false)
    private Boolean myLike;

    @Column(name = "image_path")
    private String imagePath;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "publish_date")
    private LocalDateTime publishDate;


}
