package com.social.mc_post.model;


import com.social.mc_post.dto.enums.TypeComment;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @Enumerated(EnumType.STRING)
    private TypeComment type;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "time_created", nullable = false)
    private LocalDateTime time;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "time_changed")
    private LocalDateTime timeChanged;

    @Column(name = "author_id", nullable = false)
    private String authorId;

    @Column(name = "parent_of_comment_id")
    private String parentCommentId;

    @Column(name = "comment_text")
    private String commentText;

    @ManyToOne()
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(name = "is_blocked", nullable = false)
    private Boolean isBlocked;

    @Column(name = "my_like", nullable = false)
    private Boolean myLike;

    @Column(name = "image_path")
    private String imagePath;


}
