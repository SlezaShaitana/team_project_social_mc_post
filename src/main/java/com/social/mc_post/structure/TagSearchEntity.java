package com.social.mc_post.structure;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


@Entity
@Table(name = "tag_search")
@Setter
@Getter
public class TagSearchEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    private Boolean isDeleted;

    private String name;
}
