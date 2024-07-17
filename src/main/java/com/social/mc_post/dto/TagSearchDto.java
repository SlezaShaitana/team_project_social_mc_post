package com.social.mc_post.dto;


import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TagSearchDto {

    private String id;

    private Boolean isDeleted;

    private String name;
}
