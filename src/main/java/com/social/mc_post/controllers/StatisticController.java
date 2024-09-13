package com.social.mc_post.controllers;

import com.social.mc_post.dto.RequestDto;
import com.social.mc_post.dto.ResponseStatisticDto;
import com.social.mc_post.services.StatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/post/statistic")
@RequiredArgsConstructor
public class StatisticController {

    private final StatisticService statisticService;

    @GetMapping("/post")
    public ResponseStatisticDto handlerStatisticPost(@RequestBody RequestDto requestDto,
                                                     @RequestHeader("Authorization") String headerRequestByAuth){
        return statisticService.getStatisticPost(requestDto, headerRequestByAuth);
    }

    @GetMapping("/like")
    public ResponseStatisticDto handlerStatisticLike(@RequestBody RequestDto requestDto,
                                                  @RequestHeader("Authorization") String headerRequestByAuth){
        return statisticService.getStatisticLike(requestDto, headerRequestByAuth);
    }

    @GetMapping("/comment")
    public ResponseStatisticDto handlerStatisticComment(@RequestBody RequestDto requestDto,
                                                     @RequestHeader("Authorization") String headerRequestByAuth){
        return statisticService.getStatisticComment(requestDto, headerRequestByAuth);
    }
}
