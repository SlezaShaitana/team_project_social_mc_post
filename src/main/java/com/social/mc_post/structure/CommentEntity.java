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

    private Boolean isDeleted;

    @Enumerated(EnumType.STRING)
    private TypeComment commentType;

    private Date time;

    private Date timeChanged;

    private String authorId;

    private String parentId;

    private String commentText;

    private String postId;

    private Boolean isBlocked;

    private Integer likeAmount;

    private Boolean myLike;

    private Integer commentsCount;

    private String imagePath;
}
