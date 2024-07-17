package com.social.mc_post.dto.enums;

public enum TypeComment {
    POST("комментарий к посту"),
    COMMENT("комментарий к комментарию, субкомментарий");

    private final String typeComment;

    TypeComment(String typeComment){
        this.typeComment = typeComment;
    }
}
