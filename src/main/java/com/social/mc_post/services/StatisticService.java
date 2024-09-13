package com.social.mc_post.services;

import com.social.mc_post.dto.RequestDto;
import com.social.mc_post.dto.ResponseStatisticDto;

public interface StatisticService {
    ResponseStatisticDto getStatisticPost(RequestDto requestDto, String headerRequestByAuth);
    ResponseStatisticDto getStatisticComment(RequestDto requestDto, String headerRequestByAuth);
    ResponseStatisticDto getStatisticLike(RequestDto requestDto, String headerRequestByAuth);
}
