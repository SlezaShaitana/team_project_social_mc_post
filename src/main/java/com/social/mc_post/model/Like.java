package com.social.mc_post.model;

import com.social.mc_post.dto.enums.TypeLike;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;



@Entity
@Table(name = "likes")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @Column(name = "author_id", nullable = false)
    private String authorId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "time_created", nullable = false)
    private LocalDateTime time;

    @ManyToOne()
    @JoinColumn(name = "item_post_id")
    private Post post;

    @ManyToOne()
    @JoinColumn(name = "item_comment_id")
    private Comment comment;

    @Enumerated(EnumType.STRING)
    private TypeLike type;


    @Column(name = "reaction")
    private String reaction;
}
