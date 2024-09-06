package com.social.mc_post.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
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

    private String text;

    private Boolean withFriends;

    private List<String> tags;

    private String dateFrom;

    private String dateTo;
}
