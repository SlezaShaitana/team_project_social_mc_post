package com.social.mc_post.structure;

import com.social.mc_post.dto.enums.TypeComment;
import jakarta.persistence.*;

@Entity
@Table(name = "comment_search")
public class CommentSearchEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private Boolean isDeleted;

    private TypeComment commentType;

    private String authorId;

    private String parentId;

    private String postId;
}
