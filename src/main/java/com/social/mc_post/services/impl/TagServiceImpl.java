package com.social.mc_post.services.impl;

import com.social.mc_post.dto.TagDto;
import com.social.mc_post.mapper.ConvertToListMapper;
import com.social.mc_post.mapper.TagMapper;
import com.social.mc_post.repository.TagRepository;
import com.social.mc_post.services.TagService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final ConvertToListMapper convertToList;
    private final TagMapper tagMapper;


    @Override
    public List<TagDto> getAllTags() {
        return convertToList.convertList(tagRepository.getAllTags(), tagMapper::mapToTagDto);
    }
}
