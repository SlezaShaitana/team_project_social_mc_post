package com.social.mc_post.dto;

import lombok.Data;

import java.util.Date;

@Data
public class RequestDto {

    private Date date;

    private Date firstMonth;

    private Date lastMonth;
}
