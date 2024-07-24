package com.social.mc_post.structure;

import com.social.mc_post.dto.enums.TypeComment;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Entity
@Table(name = "comment")
@Setter
@Getter
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Enumerated(EnumType.STRING)
    @Column(name = "comment_type")
    private TypeComment commentType;

    private Date time;

    @Column(name = "time_changed")
    private Date timeChanged;

    @Column(name = "author_id")
    private String authorId;

    @Column(name = "parent_id")
    private String parentId;

    @Column(name = "comment_text")
    private String commentText;

    @Column(name = "post_id")
    private String postId;

    @Column(name = "is_blocked")
    private Boolean isBlocked;

    @Column(name = "like_amount")
    private Integer likeAmount;

    @Column(name = "my_like")
    private Boolean myLike;

    @Column(name = "comments_count")
    private Integer commentsCount;

    @Column(name = "image_path")
    private String imagePath;
}
