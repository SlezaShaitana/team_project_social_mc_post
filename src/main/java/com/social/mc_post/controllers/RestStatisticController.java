package com.social.mc_post.controllers;

import com.social.mc_post.dto.RequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/post/statistic")
public class RestStatisticController {

    @GetMapping("/post")
    public ResponseEntity<?> handlerStatisticPost(@RequestBody RequestDto requestDto){
        return ResponseEntity.ok(requestDto);
    }

    @GetMapping("/like")
    public ResponseEntity<?> handlerStatisticLike(@RequestBody RequestDto requestDto){
        return ResponseEntity.ok(requestDto);
    }

    @GetMapping("/comment")
    public ResponseEntity<?> handlerStatisticComment(@RequestBody RequestDto requestDto){
        return ResponseEntity.ok(requestDto);
    }
}
