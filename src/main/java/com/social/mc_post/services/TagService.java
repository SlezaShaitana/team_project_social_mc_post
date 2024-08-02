package com.social.mc_post.services;

import com.social.mc_post.dto.TagDto;
import com.social.mc_post.structure.TagEntity;

import java.util.List;

public interface TagService {

    List<TagDto> getAllTags();
}
