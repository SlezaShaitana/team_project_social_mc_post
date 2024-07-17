package com.social.mc_post.controllers;

import com.social.mc_post.dto.TagSearchDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestTagController {


    @GetMapping("/api/v1/tag")
    public ResponseEntity<?> handlerTagSearch(){
        return ResponseEntity.ok("Hello!");
    }
}
