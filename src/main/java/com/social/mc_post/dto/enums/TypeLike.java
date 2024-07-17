package com.social.mc_post.dto.enums;

public enum TypeLike {

    POST("лайк на пост"),
    COMMENT("лайк на комментарий");

    private final String typeLike;

    TypeLike(String typeLike){
        this.typeLike = typeLike;
    }
}
