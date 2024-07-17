package com.social.mc_post.dto.enums;

public enum TypePost {

    POSTED("posted"),
    QUEUED("queued");

    private final String type;

    TypePost(String type){
        this.type = type;
    }
}
