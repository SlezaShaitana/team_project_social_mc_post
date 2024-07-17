package com.social.mc_post.structure;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "tag_search")
@Setter
@Getter
public class TagSearchEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private Boolean isDeleted;

    private String name;
}
