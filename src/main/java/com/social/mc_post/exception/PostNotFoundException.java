package com.social.mc_post.exception;

public class PostNotFoundException extends RuntimeException{

    public PostNotFoundException(String msg) {
        super(msg);
    }
}
