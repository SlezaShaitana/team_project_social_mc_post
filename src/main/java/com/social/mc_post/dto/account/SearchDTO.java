package com.social.mc_post.dto.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchDTO {
    private UUID id;
    private boolean isDeleted;
    private List<UUID> ids;
    private List<UUID> blockedByIds;
    private String author;
    private String firstName;
    private String lastName;
    private String city;
    private String country;
    private boolean isBlocked;
    private String statusCode;
    private Integer ageTo;
    private Integer ageFrom;
}