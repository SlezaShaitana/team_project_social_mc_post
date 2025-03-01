package com.social.mc_post.dto;

import lombok.*;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostSearchDto {

    private String id;

    private Boolean isDeleted;

    private List<String> ids;

    private List<String> accountIds;

    private List<String> blockedIds;

    private String author;

    private Boolean withFriends;

    private List<String> tags;

    private Date dateFrom;

    private Date dateTo;
}
