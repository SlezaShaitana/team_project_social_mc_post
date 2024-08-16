package com.social.mc_post.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserShortDto {
    private String id;
    private String email;
    private List<String> roles;

}

