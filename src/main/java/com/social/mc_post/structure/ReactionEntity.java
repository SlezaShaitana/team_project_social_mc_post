package com.social.mc_post.structure;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "reaction")
@Setter
@Getter
public class ReactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String reactionType;

    private Integer count;
}
