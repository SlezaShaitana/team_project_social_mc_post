package com.social.mc_post.controllers;

import com.social.mc_post.dto.TagDto;
import com.social.mc_post.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping("/api/v1/tag")
    public List<TagDto> handlerTagSearch(){
        return tagService.getAllTags();
    }
}
