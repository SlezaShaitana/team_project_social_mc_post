package com.social.mc_post.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResponseStatisticDto {
    private LocalDateTime time;
    private Integer countResult;
    private LocalDateTime periodFrom;
    private LocalDateTime periodTo;
}
