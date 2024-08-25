package com.social.mc_post.controllers;

import com.social.mc_post.dto.TagDto;
import com.social.mc_post.dto.TagSearchDto;
import com.social.mc_post.services.TagService;
import com.social.mc_post.structure.TagEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RestTagController {

    private final TagService tagService;

    @GetMapping("/api/v1/tag")
    public List<TagDto> handlerTagSearch(){
        return tagService.getAllTags();
    }
}
